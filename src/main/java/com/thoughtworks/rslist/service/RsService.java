package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.po.RsEventPO;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RsService {
    @Autowired
    private RsEventRepository rsRepository;

    @Autowired
    private UserEntityRepository userEntityRepository;

    public int addEvent(RsEvent rsEvent) {
        UserPO userPO = userEntityRepository.findUserEntityById(rsEvent.getUserId());
        if (userPO == null)
            return 0;
        RsEventPO rsEventPO = RsEventPO.builder().eventName(rsEvent.getEventName())
                .keyword(rsEvent.getKeyWord()).userPO(userPO).build();

        return rsRepository.save(rsEventPO).getId();
    }

    public List<RsEventPO> findAll() {
        return rsRepository.findAll();
    }

    public RsEventPO findById(int id) {
        return rsRepository.findRsEventEntityById(id);
    }

    public RsEventPO updateById(int id, RsEvent rsEvent) {
        UserPO userPO = userEntityRepository.findUserEntityById(rsEvent.getUserId());
        RsEventPO oldEvent = rsRepository.findRsEventEntityById(id);
        if (!(userPO == null)) {
            RsEventPO rsEventPO = RsEventPO.builder()
                    .eventName((rsEvent.getEventName() == null ? oldEvent.getEventName() : rsEvent.getEventName()))
                    .keyword((rsEvent.getKeyWord() == null ? oldEvent.getKeyword() : rsEvent.getKeyWord()))
                    .id(id)
                    .userPO(userPO).build();
            return rsRepository.save(rsEventPO);
        }
        return null;
    }
}
