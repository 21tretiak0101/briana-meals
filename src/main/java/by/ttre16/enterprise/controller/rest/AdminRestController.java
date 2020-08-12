package by.ttre16.enterprise.controller.rest;

import by.ttre16.enterprise.controller.AbstractUserController;
import by.ttre16.enterprise.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.AccessControlException;
import java.util.List;

import static by.ttre16.enterprise.util.web.UrlUtil.ADMIN_REST_URL;

@RestController
@RequestMapping(ADMIN_REST_URL)
public class AdminRestController extends AbstractUserController {

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<User> getAll() {
        if (isAdmin()) {
            return super.getAll();
        } else {
            throw new AccessControlException(
                    "This user doesn't have administrator rights");
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        if (isAdmin()) {
            super.delete(id);
        }
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody User user, @PathVariable Integer id) {
        if (isAdmin()) {
            super.update(user, id);
        }
    }

    @GetMapping("/by")
    @ResponseStatus(HttpStatus.OK)
    public User getByEmail(@RequestParam String email) {
        if (isAdmin()) {
            return super.getByMail(email);
        } else {
            throw new AccessControlException(
                    "This user doesn't have administrator rights");
        }
    }
}
