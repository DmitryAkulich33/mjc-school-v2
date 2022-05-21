package com.epam.esm.dao;

import com.epam.esm.config.TestConfig;
import com.epam.esm.domain.Tag;
import com.epam.esm.exception.TagDuplicateException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
@SqlGroup({
        @Sql(scripts = "/drop_tables.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
        @Sql(scripts = "/create_tables.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
        @Sql(scripts = "/init_tables.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
class TagDaoImplTest {
    private static final Long ID_1 = 1L;
    private static final Long ID_2 = 2L;
    private static final Long ID_3 = 3L;
    private static final Long ID_10 = 10L;
    private static final String NOT_FOUND_NAME = "wrong";
    private static final String NAME_FOOD = "food";
    private static final String NAME_DELIVERY = "delivery";
    private static final String NAME_SPA = "spa";
    private static final Integer STATE_0 = 0;
    private static final Integer STATE_1 = 1;

    private Tag tag1;
    private Tag tag2;
    private Tag tag3;

    @Autowired
    private TagDao tagDao;

    @BeforeEach
    public void setUp() {
        tag1 = Tag.builder()
                .id(ID_1)
                .name(NAME_FOOD)
                .state(STATE_0)
                .build();

        tag2 = Tag.builder()
                .id(ID_2)
                .name(NAME_DELIVERY)
                .state(STATE_0)
                .build();

        tag3 = Tag.builder()
                .name(NAME_SPA)
                .build();
    }

    @Test
    public void testGetTagById() {
        Optional<Tag> expected = Optional.of(tag1);
        Optional<Tag> actual = tagDao.getTagById(ID_1);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetTagById_TagNotFound() {
        Optional<Tag> expected = Optional.empty();

        Optional<Tag> actual = tagDao.getTagById(ID_10);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetAllTags() {
        List<Tag> expected = new ArrayList<>(Arrays.asList(tag1, tag2));

        List<Tag> actual = tagDao.getAllTags();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testDeleteTag() {
        tag2.setState(STATE_1);
        List<Tag> expected = new ArrayList<>(Collections.singletonList(tag1));

        tagDao.deleteTag(ID_2);

        List<Tag> actual = tagDao.getAllTags();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testGetTagByName() {
        Optional<Tag> expected = Optional.of(tag1);
        Optional<Tag> actual = tagDao.getTagByName(NAME_FOOD);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testGetTagByName_TagNotFound() {
        Optional<Tag> expected = Optional.empty();

        Optional<Tag> actual = tagDao.getTagByName(NOT_FOUND_NAME);

        assertEquals(expected, actual);
    }

    @Test
    public void testCreateTag() {
        Tag expected = Tag.builder()
                .id(ID_3)
                .name(NAME_SPA)
                .state(STATE_0)
                .build();

        Tag actual = tagDao.createTag(tag3);

        assertEquals(expected, actual);
    }

    @Test
    public void testCreateTag_TagDuplicateException() {
        assertThrows(TagDuplicateException.class, () -> {
            tagDao.createTag(tag2);
        });
    }

    @Test
    public void testGetTagsFromCertificate() {
        List<Tag> expected = new ArrayList<>(Arrays.asList(tag1, tag2));

        List<Tag> actual = tagDao.getTagsFromCertificate(ID_1);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetTagsFromCertificate_EmptyResult() {
        List<Tag> actual = tagDao.getTagsFromCertificate(ID_10);

        assertEquals(0, actual.size());
    }

    @Test
    public void testCreateTagCertificate() {
        List<Tag> expected = new ArrayList<>(Arrays.asList(tag1, tag2));

        tagDao.createTagCertificate(ID_2, ID_2);

        List<Tag> actual = tagDao.getTagsFromCertificate(ID_2);

        assertEquals(expected, actual);
    }

    @Test
    public void testDeleteTagCertificate() {
        tagDao.deleteTagCertificate(ID_1, ID_2);

        List<Tag> actual = tagDao.getTagsFromCertificate(ID_2);

        assertEquals(0, actual.size());
    }
}