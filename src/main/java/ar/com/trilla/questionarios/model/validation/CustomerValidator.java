package ar.com.trilla.questionarios.model.validation;

import ar.com.trilla.questionarios.model.Customer;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class CustomerValidator {
    @Inject
    private Validator validator;

    public Result validate(Customer customer) {
        Set<ConstraintViolation<Customer>> violations = validator.validate(customer);

        if (violations.isEmpty()) {
            return new Result("Customer is valid! It was validated by manual validation.");
        } else {
            return new Result(violations);
        }
    }

    public static class Result {
        Result(String message) {
            this.success = true;
            this.message = message;
        }

        Result(Set<? extends ConstraintViolation<?>> violations) {
            this.success = false;
            this.message = violations.stream()
                    .map(cv -> cv.getMessage())
                    .collect(Collectors.joining(", "));
        }

        private String message;
        private boolean success;

        public String getMessage() {
            return message;
        }

        public boolean isSuccess() {
            return success;
        }
    }
}
