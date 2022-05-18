package com.epam.esm.service.validator;

import com.epam.esm.domain.Tag;
import com.epam.esm.exception.TagValidatorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TagValidatorImpl implements TagValidator {
    private final ValidatorHelper validatorHelper;
    private static final String TAG_NAME_REGEX = "^\\S{1,70}$";

    @Autowired
    public TagValidatorImpl(ValidatorHelper validatorHelper) {
        this.validatorHelper = validatorHelper;
    }

    @Override
    public void validateTag(Tag tag) {
        validateTagName(tag.getName());
    }

    @Override
    public void validateTagsToCreateCertificate(List<Tag> tags) {
        if (tags != null && !tags.isEmpty()) {
            tags.forEach(tag -> validateTagName(tag.getName()));
            return;
        }
        throw new TagValidatorException("Entered tag name isn't valid");
    }

    @Override
    public void validateTagsToUpdateCertificate(List<Tag> tags) {
        if (tags != null) {
            if (tags.isEmpty()) {
                throw new TagValidatorException("Entered list of tags is empty");
            }
            tags.forEach(tag -> validateTagName(tag.getName()));
        }
    }

    private void validateTagName(String name) {
        if (name != null && validatorHelper.validateStringParameter(TAG_NAME_REGEX, name)) {
            return;
        }
        throw new TagValidatorException("Entered tag name isn't valid");
    }
}
