package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.RsEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class RsController {
    private List<RsEvent> rsList = initList();

    private List<RsEvent> initList(){
        List<RsEvent> list = new ArrayList<>();
        list.add(new RsEvent("第一条事件", "经济"));
        list.add(new RsEvent("第二条事件", "社会"));
        list.add(new RsEvent("第三条事件", "民生"));
        return list;
    }

    @GetMapping("/rs/list")
    public List<RsEvent> getList(@RequestParam(required = false) Integer start,
                                 @RequestParam(required = false) Integer end) {
        if (null == start || null == end){
            return rsList;
        }
        return rsList.subList(start - 1, end);
    }

    @GetMapping("/rs/{index}")
    public RsEvent getOne(@PathVariable int index) {
        return rsList.get(index - 1);
    }
}
