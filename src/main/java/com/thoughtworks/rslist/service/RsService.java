
package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.exception.InvalidIndexException;
import com.thoughtworks.rslist.exception.InvalidRequestParamException;
import com.thoughtworks.rslist.repositories.RsEventRepository;
import com.thoughtworks.rslist.repositories.UserListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RsService {
    @Autowired
    private RsEventRepository rsRepository;

    @Autowired
    private UserListRepository userRepository;

    public List<RsEvent> getAllRs() {
        return rsRepository.getRsList();
    }

    public List<RsEvent> getSubRs(int start, int end) throws InvalidRequestParamException {
        List<RsEvent> rsList = rsRepository.getRsList();
        int size = rsList.size();
        if (start < 1 || start > size || end < 1 || end > size)
            throw new InvalidRequestParamException();
        return rsRepository.getRsList().subList(start - 1, end);
    }

    public RsEvent getOne(int index) throws InvalidIndexException {
        List<RsEvent> rsList = rsRepository.getRsList();
        if (index < 0 || index > rsList.size())
            throw new InvalidIndexException();
        return rsRepository.getOne(index);
    }

    public int addOneEvent(RsEvent rsEvent) {
        User user = rsEvent.getUser();
        if (!userRepository.isExist(user))
            userRepository.addUser(user);
        return rsRepository.addEvent(rsEvent);
    }

    public void updateEventById(int id, RsEvent rsEventUpdate) {
        rsRepository.updateEventById(id, rsEventUpdate);
    }

    public void deleteEventById(int id) {
        rsRepository.deleteEventById(id);
    }
}
