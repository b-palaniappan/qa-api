package io.c12.bala.api.exception;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.hibernate.validator.internal.engine.path.PathImpl;

import javax.validation.ConstraintViolation;
import javax.ws.rs.core.Response;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.CUSTOM, property = "error", visible = true)
@JsonTypeIdResolver(LowerCaseClassNameResolver.class)
public class ApiError {

    @Schema(name="status", title = "HTTP Response status", description = "HTTP Response status code", readOnly = true, example = "NOT_FOUND")
    private Response.Status status;

    @Schema(name = "timestamp", title = "Timestamp of error", description = "Time stamp in UTC when the system return error", readOnly = true)
    private Instant timestamp;

    @Schema(name = "message", title = "Failure message", description = "Brief failure message", readOnly = true, example = "User not found")
    private String message;

    @Schema(name = "debugMessage", title = "Failure debug message", description = "More detailed message about failure", readOnly = true, example = "User not found for id usr_F3NhmbnuE7DuN46reypGo")
    private String debugMessage;

    @Schema(name = "subErrors", title = "List of sub errors", description = "List of validation errors", readOnly = true, type = SchemaType.ARRAY, allOf = {ApiValidationError.class})
    private List<ApiSubError> subErrors;

    // Constructors...
    private ApiError() {
        timestamp = Instant.now();
    }

    ApiError(Response.Status status) {
        this();
        this.status = status;
    }

    ApiError(Response.Status status, String message) {
        this();
        this.status = status;
        this.message = message;
    }

    ApiError(Response.Status status, Throwable ex) {
        this();
        this.status = status;
        this.message = "Unexpected error";
        this.debugMessage = ex.getLocalizedMessage();
    }

    ApiError(Response.Status status, String message, Throwable ex) {
        this();
        this.status = status;
        this.message = message;
        this.debugMessage = ex.getLocalizedMessage();
    }

    private void addSubError(ApiSubError subError) {
        if (subErrors == null) {
            subErrors = new ArrayList<>();
        }
        subErrors.add(subError);
    }

    private void addValidationError(String object, String field, Object rejectedValue, String message) {
        addSubError(new ApiValidationError(object, field, rejectedValue, message));
    }

    private void addValidationError(String object, String message) {
        addSubError(new ApiValidationError(object, message));
    }
//
//    private void addValidationError(FieldError fieldError) {
//        this.addValidationError(
//                fieldError.getObjectName(),
//                fieldError.getField(),
//                fieldError.getRejectedValue(),
//                fieldError.getDefaultMessage());
//    }
//
//    void addValidationErrors(List<FieldError> fieldErrors) {
//        fieldErrors.forEach(this::addValidationError);
//    }
//
//    private void addValidationError(ObjectError objectError) {
//        this.addValidationError(
//                objectError.getObjectName(),
//                objectError.getDefaultMessage());
//    }
//
//    void addValidationError(List<ObjectError> globalErrors) {
//        globalErrors.forEach(this::addValidationError);
//    }

    /**
     * Utility method for adding error of ConstraintViolation. Usually when a @Validated validation fails.
     *
     * @param cv the ConstraintViolation
     */
    private void addValidationError(ConstraintViolation<?> cv) {
        this.addValidationError(
                cv.getRootBeanClass().getSimpleName(),
                ((PathImpl) cv.getPropertyPath()).getLeafNode().asString(),
                cv.getInvalidValue(),
                cv.getMessage());
    }

    void addValidationErrors(Set<ConstraintViolation<?>> constraintViolations) {
        constraintViolations.forEach(this::addValidationError);
    }

    abstract static class ApiSubError {

    }

    /**
     * Sub class of Api Error to handle Validation Errors.
     * TODO: Need to figure our how to return validation error.
     */
    @Data
    @EqualsAndHashCode(callSuper = false)
    @AllArgsConstructor
    static class ApiValidationError extends ApiSubError {

        @Schema(name="object", title = "Name of validation failure object", example = "userDto")
        private String object;

        @Schema(name = "field", title = "Field of validation failure", example = "email")
        private String field;

        @Schema(name = "rejectedValue", title = "Rejected value from request payload", type = SchemaType.STRING, example = "hello.world@example")
        private Object rejectedValue;

        @Schema(name = "message", title = "Reason for the validation failure", example = "Invalid email format")
        private String message;

        ApiValidationError(String object, String message) {
            this.object = object;
            this.message = message;
        }
    }
}

class LowerCaseClassNameResolver extends TypeIdResolverBase {

    @Override
    public String idFromValue(Object value) {
        return value.getClass().getSimpleName().toLowerCase();
    }

    @Override
    public String idFromValueAndType(Object value, Class<?> suggestedType) {
        return idFromValue(value);
    }

    @Override
    public JsonTypeInfo.Id getMechanism() {
        return JsonTypeInfo.Id.CUSTOM;
    }
}
