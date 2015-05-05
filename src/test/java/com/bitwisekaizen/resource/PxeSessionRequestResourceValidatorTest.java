package com.bitwisekaizen.resource;


import com.bitwisekaizen.validation.DtoValidator;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.HashSet;
import java.util.Set;

import static com.bitwisekaizen.builder.PxeSessionRequestResourceBuilder.aSessionRequest;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.IsCollectionContaining.hasItem;

@Test
public class PxeSessionRequestResourceValidatorTest {
    private static final Logger logger = LoggerFactory.getLogger(PxeSessionRequestResourceValidatorTest.class);

    private DtoValidator dtoValidator;

    @BeforeMethod
    public void beforeMethod() {
        dtoValidator = new DtoValidator();
    }

    @Test
    public void validationShouldPassForValidMacAddress() {
        assertThat(getViolations(aSessionRequest("0a-1b-3c-4d-5e-6a").build()), hasSize(0));
        assertThat(getViolations(aSessionRequest("0a:1b:3c:4d:5e:6a").build()), hasSize(0));
    }

    @Test
    public void validationShouldFailForEmptyMacAddress() {
        Set<ConstraintViolation<?>> violations = getViolations(aSessionRequest("").build());

        assertThat(violations, hasSize(2));
        assertThat(violations, hasItem(matchingMessage("mac.address.empty")));
        assertThat(violations, hasItem(matchingMessage("mac.address.malformed")));
    }

    @Test
    public void validationShouldFailForNullMacAddress() {
        Set<ConstraintViolation<?>> violations = getViolations(aSessionRequest(null).build());

        assertThat(violations, hasSize(2));
        assertThat(violations, hasItem(matchingMessage("mac.address.empty")));
        assertThat(violations, hasItem(matchingMessage("mac.address.malformed")));
    }

    @Test
    public void validationShouldFailForMalformedMacAddress() {
        assertHasSingleViolation(getViolations(aSessionRequest("bad one").build()), "mac.address.malformed");

        assertHasSingleViolation(getViolations(aSessionRequest("0a-1b-aaa").build()), "mac.address.malformed");

        assertHasSingleViolation(getViolations(aSessionRequest("za-1b-3c-4d-5e-6a").build()), "mac.address.malformed");

        assertHasSingleViolation(getViolations(aSessionRequest("za:1b:3c:4d:5e:6a").build()), "mac.address.malformed");
    }

    private void assertHasSingleViolation(Set<ConstraintViolation<?>> violations, String message) {
        assertThat(violations, hasSize(1));
        assertThat(violations, hasItem(matchingMessage(message)));
    }

    protected Set<ConstraintViolation<?>> getViolations(Object objectToValidate) {
        try {
            dtoValidator.validate(objectToValidate);
            return new HashSet<ConstraintViolation<?>>();
        } catch (ConstraintViolationException e) {
            return e.getConstraintViolations();
        }
    }

    private TypeSafeMatcher<ConstraintViolation<?>> matchingMessage(final String expectedMessage) {
        return new TypeSafeMatcher<ConstraintViolation<?>>() {
            @Override
            protected boolean matchesSafely(ConstraintViolation<?> constraintViolation) {
                return constraintViolation.getMessage().equals(expectedMessage);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText(expectedMessage);
            }
        };
    }
}
