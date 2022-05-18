package com.epam.esm.service.validator;

import com.epam.esm.domain.Tag;

import java.util.List;

public interface TagValidator {
    void validateTag(Tag tag);

    void validateTagsToCreateCertificate(List<Tag> tags);

    void validateTagsToUpdateCertificate(List<Tag> tags);
}
