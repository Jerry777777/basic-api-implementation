
package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.po.RsEventPO;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RsService {
    @Autowired
    private RsEventRepository rsRepository;

    @Autowired
    private UserRepository userRepository;

    public int addEvent(RsEvent rsEvent){
        UserPO userEntity = userRepository.findUserPOById(rsEvent.getUserId());
        if (userEntity == null)
            return 0;

        RsEventPO rsEventEntity = RsEventPO.builder().eventName(rsEvent.getEventName())
                .keyword(rsEvent.getKeyWord()).userEntity(userEntity).build();

        return rsRepository.save(rsEventEntity).getId();
    }

    public List<RsEventPO> findAll() {
        return rsRepository.findAll();
    }

    public RsEventPO findById(int id) {
       return rsRepository.findById(id);
    }
}
