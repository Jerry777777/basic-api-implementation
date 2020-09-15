package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.RsEvent;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class RsController {
    private List<RsEvent> rsList = initList();

    private List<RsEvent> initList() {
        List<RsEvent> list = new ArrayList<>();
        list.add(new RsEvent("第一条事件", "经济"));
        list.add(new RsEvent("第二条事件", "社会"));
        list.add(new RsEvent("第三条事件", "民生"));
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
    public void addOneEvent(@RequestBody String rsEventString) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        RsEvent rsEvent = objectMapper.readValue(rsEventString, RsEvent.class);
        rsList.add(rsEvent);
    }

    @PutMapping("/rs/update")
    public void updateRsEvent(@RequestBody String rsEventString) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        RsEvent rsEventUpdate = objectMapper.readValue(rsEventString, RsEvent.class);

        rsList.forEach(rsEvent -> {
            if (rsEvent.getEventName().equals(rsEventUpdate.getEventName()))
                rsEvent.setKeyWord(rsEventUpdate.getKeyWord());
        });
    }
}
