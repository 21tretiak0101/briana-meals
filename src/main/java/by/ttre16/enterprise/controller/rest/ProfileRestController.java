package by.ttre16.enterprise.controller.rest;

import by.ttre16.enterprise.controller.AbstractUserController;
import by.ttre16.enterprise.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static by.ttre16.enterprise.util.web.UrlUtil.PROFILE_REST_URL;

@RestController
@RequestMapping(PROFILE_REST_URL)
public class ProfileRestController extends AbstractUserController {
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public User get() {
        return super.get(securityUtil.getAuthUserId());
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody User user) {
        super.update(user, securityUtil.getAuthUserId());
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete() {
        super.delete(securityUtil.getAuthUserId());
    }
}
