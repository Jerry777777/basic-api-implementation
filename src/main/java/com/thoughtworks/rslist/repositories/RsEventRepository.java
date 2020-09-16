package com.thoughtworks.rslist.repositories;

import com.thoughtworks.rslist.domain.Gender;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class RsEventRepository {
    private List<RsEvent> rsList = initList();

    private List<RsEvent> initList() {
        List<RsEvent> list = new ArrayList<>();
        list.add(new RsEvent(1, "第一条事件", "经济",
                new User("userA", Gender.MALE, 39, "A@aaa.com", "11234567890")));
        list.add(new RsEvent(2, "第二条事件", "社会",
                new User("userB", Gender.FEMALE, 32, "B@aaa.com", "11234567891")));
        list.add(new RsEvent(3, "第三条事件", "民生",
                new User("userC", Gender.Transgender, 21, "C@aaa.com", "11234567892")));
        return list;
    }

    public List<RsEvent> getRsList() {
        return rsList;
    }

    public void setRsList(List<RsEvent> rsList) {
        this.rsList = rsList;
    }

    public RsEvent getOne(int index) {
        return rsList.get(index - 1);
    }

    public void updateEventById(int id, RsEvent rsEventUpdate) {
        if (rsEventUpdate.getEventName() != null)
            rsList.get(id - 1).setEventName(rsEventUpdate.getEventName());
        if (rsEventUpdate.getKeyWord() != null)
            rsList.get(id - 1).setKeyWord(rsEventUpdate.getKeyWord());
    }

    public void deleteEventById(int id) {
        if (id <= rsList.size())
            rsList.remove(id - 1);
    }

    public int addEvent(RsEvent rsEvent){
        rsList.add(rsEvent);
        return rsList.size();
    }
}
