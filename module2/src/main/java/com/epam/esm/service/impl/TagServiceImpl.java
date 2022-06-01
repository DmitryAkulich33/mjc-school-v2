package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Order;
import com.epam.esm.domain.Tag;
import com.epam.esm.domain.User;
import com.epam.esm.exception.PaginationException;
import com.epam.esm.exception.TagNotFoundException;
import com.epam.esm.service.TagService;
import com.epam.esm.service.UserService;
import com.epam.esm.util.OffsetCalculator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

@Service
public class TagServiceImpl implements TagService {
    private final TagDao tagDao;
    private final UserService userService;

    private static Logger log = LogManager.getLogger(TagServiceImpl.class);

    @Autowired
    public TagServiceImpl(TagDao tagDao, UserService userService) {
        this.tagDao = tagDao;
        this.userService = userService;
    }

    @Override
    public Tag getTagById(Long id) {
        log.debug(String.format("Getting tag with id: %d", id));
        Optional<Tag> optionalTag = tagDao.getTagById(id);
        return optionalTag.orElseThrow(() -> new TagNotFoundException("tag.id.not.found", id));
    }

    @Transactional
    @Override
    public void deleteTag(Long id) {
        log.debug(String.format("Removing tag with id:  %d", id));
        getTagById(id);
        tagDao.deleteTag(id);
    }

    @Transactional
    @Override
    public Tag createTag(Tag tag) {
        log.debug(String.format("Creating tag with name:  %s", tag.getName()));
        return tagDao.createTag(tag);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Tag> getTags(Integer pageNumber, Integer pageSize) {
        log.debug(String.format("Getting all tags - pageNumber: %d, pageSize: %d", pageNumber, pageSize));
        if (pageNumber != null && pageSize != null) {
            Integer offset = OffsetCalculator.calculateOffset(pageNumber, pageSize);
            return tagDao.getTags(offset, pageSize);
        } else {
            throw new PaginationException("pagination.not.valid.data", pageNumber, pageSize);
        }
    }

    @Override
    public Tag getTheMostUsedTag() {
        log.debug("Service: search the most used Tag");
        User user = userService.getUserWithTheLargeSumOrders();
        List<Certificate> userCertificates = getCertificatesFromUserOrders(user.getOrders());
        List<Tag> userTags = getTagsFromUserCertificate(userCertificates);

        return userTags.stream()
                .collect(groupingBy(x -> x, counting()))
                .entrySet().stream()
                .max(comparingByValue())
                .get().getKey();
    }

    private List<Certificate> getCertificatesFromUserOrders(List<Order> orders) {
        List<Certificate> certificates = new ArrayList<>();
        orders.forEach(s -> certificates.addAll(s.getCertificates()));
        return certificates;
    }

    private List<Tag> getTagsFromUserCertificate(List<Certificate> certificates) {
        List<Tag> tags = new ArrayList<>();
        certificates.forEach(s -> tags.addAll(s.getTags()));
        return tags;
    }

}
