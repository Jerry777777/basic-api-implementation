package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.repositories.RsEventRepository;
import com.thoughtworks.rslist.repositories.UserListRepository;
import com.thoughtworks.rslist.service.RsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RsController {
    @Autowired
    private RsService rsService;

    @GetMapping("/rs/list")
    public List<RsEvent> getList(@RequestParam(required = false) Integer start,
                                 @RequestParam(required = false) Integer end) {
        if (null == start || null == end) {
            return rsService.getAllRs();
        }
        return rsService.getSubRs(start, end);
    }

    @GetMapping("/rs/{index}")
    public RsEvent getOne(@PathVariable int index) {
        if (index > 0)
            return rsService.getOne(index);
        else
            return null;
    }

    @PostMapping("/rs/add")
    public void addOneEvent(@RequestBody @Validated RsEvent rsEvent) {
        rsService.addOneEvent(rsEvent);
    }

    @PutMapping("/rs/update/{id}")
    public void updateRsEventByIndex(@PathVariable int id, @RequestBody RsEvent rsEventUpdate) {
        rsService.updateEventById(id, rsEventUpdate);
    }

    @DeleteMapping("/rs/delete/{id}")
    public void deleteRsEventByIndex(@PathVariable int id) {
        rsService.deleteEventById(id);
    }
}
