package com.epam.esm.service.impl;

import com.epam.esm.dao.UserDao;
import com.epam.esm.domain.User;
import com.epam.esm.exception.PaginationException;
import com.epam.esm.exception.UserNotFoundException;
import com.epam.esm.service.UserService;
import com.epam.esm.util.OffsetCalculator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    private static Logger log = LogManager.getLogger(UserServiceImpl.class);

    @Autowired
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User getUserById(Long id) {
        log.debug(String.format("Search user by id %d", id));
        Optional<User> optionalUser = userDao.getUserById(id);
        return optionalUser.orElseThrow(() -> new UserNotFoundException("user.id.not.found", id));
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> getUsers(Integer pageNumber, Integer pageSize) {
        log.debug("Search all users.");
        if (pageNumber != null && pageSize != null) {
            Integer offset = OffsetCalculator.calculateOffset(pageNumber, pageSize);
            return userDao.getUsers(offset, pageSize);
        } else {
            throw new PaginationException("pagination.not.valid.data", pageNumber, pageSize);
        }
    }

    @Override
    public User getUserWithTheLargeSumOrders() {
        return userDao.getUserWithTheLargeSumOrders();
    }
}
