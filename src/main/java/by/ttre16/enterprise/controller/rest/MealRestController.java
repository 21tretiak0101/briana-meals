package by.ttre16.enterprise.controller.rest;

import by.ttre16.enterprise.controller.AbstractMealController;
import by.ttre16.enterprise.dto.MealTo;
import by.ttre16.enterprise.model.Meal;
import by.ttre16.enterprise.security.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static by.ttre16.enterprise.util.web.UrlUtil.MEAL_REST_URL;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentContextPath;

@RestController
@RequestMapping(MEAL_REST_URL)
public class MealRestController extends AbstractMealController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    private final SecurityUtil securityUtil;

    @Autowired
    public MealRestController(SecurityUtil securityUtil) {
        this.securityUtil = securityUtil;
    }

    @GetMapping
    public List<Meal> getAll() {
        return super.getAll(getUserId());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Meal> create(@RequestBody Meal meal) {
        Meal created = super.create(meal, getUserId());
        URI newResourceUri = fromCurrentContextPath()
                .path(MEAL_REST_URL + "{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(newResourceUri).body(created);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        super.delete(getUserId(), id);
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody Meal meal, @PathVariable Integer id) {
        super.update(getUserId(), meal, id);
    }

    @GetMapping("/filter")
    public List<MealTo> getWithExcess(@RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate, @RequestParam LocalTime startTime,
            @RequestParam LocalTime endTime) {
        return super.getBetween(startDate, endDate, startTime, endTime,
                getUserId(), securityUtil.getAuthUserCaloriesPerDay());
    }

    private Integer getUserId() {
        return securityUtil.getAuthUserId();
    }
}
