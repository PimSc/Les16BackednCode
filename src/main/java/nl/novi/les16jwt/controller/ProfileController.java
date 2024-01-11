package nl.novi.les16jwt.controller;

import nl.novi.les16jwt.dto.ProfileDto;
import nl.novi.les16jwt.model.Profile;
import nl.novi.les16jwt.repository.ProfileRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/profiles")
public class ProfileController {

    // No ProfileService used in demo code!

    private final ProfileRepository repos;

    public ProfileController(ProfileRepository repos) {
        this.repos = repos;
    }

    @PostMapping
    public ResponseEntity<Profile> createProfile(@RequestBody ProfileDto profileDto) {
        Profile profile = new Profile();
        profile.setUsername(profileDto.username);
        profile.setFirstname(profileDto.firstname);
        profile.setLastname(profileDto.lastname);
        profile.setAddress(profileDto.address);
        profile.setBankAccount(profileDto.bankaccount);

        this.repos.save(profile);

        return ResponseEntity.created(null).body(profile);
    }

    @GetMapping("/{username}")
    public ResponseEntity<Object> getProfile(@AuthenticationPrincipal UserDetails userDetails, @PathVariable String username) {
        if (Objects.equals(userDetails.getUsername(), username)) {
            Profile profile = this.repos.findById(username).get(); // happy flow
            ProfileDto profileDto = new ProfileDto();
            profileDto.username = profile.getUsername();
            profileDto.firstname = profile.getFirstname();
            profileDto.lastname = profile.getLastname();
            profileDto.address = profile.getAddress();
            profileDto.bankaccount = profile.getBankAccount();
            return ResponseEntity.ok(profileDto);
        } else {
// Moet exception worden!
            return ResponseEntity.ok("INCORRECTE LOGIN");
        }
    }
}