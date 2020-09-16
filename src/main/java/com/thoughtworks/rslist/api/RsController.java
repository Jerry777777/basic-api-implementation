package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.service.RsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RsController {
    @Autowired
    private RsService rsService;

    @GetMapping("/rs/list")
    public ResponseEntity<List<RsEvent>> getList(@RequestParam(required = false) Integer start,
                                                 @RequestParam(required = false) Integer end) {
        if (null == start || null == end) {
            return ResponseEntity.ok().body(rsService.getAllRs());
        }
        return ResponseEntity.ok().body(rsService.getSubRs(start, end));
    }

    @GetMapping("/rs/{index}")
    public ResponseEntity<RsEvent> getOne(@PathVariable int index) {
        if (index > 0)
            return ResponseEntity.ok().body(rsService.getOne(index));
        else
            return ResponseEntity.ok().body(null);
    }

    @PostMapping("/rs/add")
    public ResponseEntity<String> addOneEvent(@RequestBody @Validated RsEvent rsEvent) {
        int addIndex = rsService.addOneEvent(rsEvent);
        return ResponseEntity.created(null)
                .header("addIndex", String.valueOf(addIndex)).build();
    }

    @PutMapping("/rs/update/{id}")
    public ResponseEntity <String> updateRsEventById(@PathVariable int id, @RequestBody RsEvent rsEventUpdate) {
        rsService.updateEventById(id, rsEventUpdate);
        return ResponseEntity.ok().body(null);
    }

    @DeleteMapping("/rs/delete/{id}")
    public ResponseEntity<String> deleteRsEventById(@PathVariable int id) {
        rsService.deleteEventById(id);
        return ResponseEntity.ok().body(null);
    }
}
