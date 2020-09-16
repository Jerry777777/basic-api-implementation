package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.domain.Gender;
import com.thoughtworks.rslist.repositories.UserListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class RsController {
    private List<RsEvent> rsList = initList();

    @Autowired
    private UserListRepository userListRepository;

    private List<RsEvent> initList() {
        List<RsEvent> list = new ArrayList<>();
        list.add(new RsEvent(1,"第一条事件", "经济", new User("userA", Gender.MALE, 39, "A@aaa.com", "11234567890")));
        list.add(new RsEvent(2,"第二条事件", "社会", new User("userB", Gender.FEMALE, 32, "B@aaa.com", "11234567891")));
        list.add(new RsEvent(3,"第三条事件", "民生", new User("userC", Gender.Transgender, 21, "C@aaa.com", "11234567892")));
        return list;
    }

    @GetMapping("/rs/list")
    public List<RsEvent> getList(@RequestParam(required = false) Integer start,
                                 @RequestParam(required = false) Integer end) {
        if (null == start || null == end) {
            return rsList;
        }
        return rsList.subList(start - 1, end);
    }

    @GetMapping("/rs/{index}")
    public RsEvent getOne(@PathVariable int index) {
        return rsList.get(index - 1);
    }

    @PostMapping("/rs/add")
    public void addOneEvent(@RequestBody @Validated RsEvent rsEvent) {
        User user = rsEvent.getUser();
        if (!userListRepository.isExist(user))
            userListRepository.addUser(user);
        rsList.add(rsEvent);
    }

    @PutMapping("/rs/update/{id}")
    public void updateRsEventByIndex(@PathVariable int id, @RequestBody RsEvent rsEventUpdate) {

        if (rsEventUpdate.getEventName() != null) {
            rsList.get(id-1).setEventName(rsEventUpdate.getEventName());
        }
        if (rsEventUpdate.getKeyWord() != null) {
            rsList.get(id-1).setKeyWord(rsEventUpdate.getKeyWord());
        }
    }

    @DeleteMapping("/rs/delete/{id}")
    public void deleteRsEventByIndex(@PathVariable int id) {
        if (id <= rsList.size())
            rsList.remove(id - 1);
    }
}
