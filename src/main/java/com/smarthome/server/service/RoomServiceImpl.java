package com.smarthome.server.service;

import com.smarthome.server.dao.DeviceConfigurationDao;
import com.smarthome.server.dao.RoomConfigurationDao;
import com.smarthome.server.entity.DeviceConfigurationModel;
import com.smarthome.server.entity.Responses.SimpleResponse;
import com.smarthome.server.entity.RoomConfigurationModel;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@AllArgsConstructor
public class RoomServiceImpl implements RoomService {

    private DeviceConfigurationDao deviceConfigurationDao;
    private SimpMessagingTemplate simpMessagingTemplate;
    private RoomConfigurationDao roomConfigurationDao;


    public void addRoom(String roomName, String main) {
        RoomConfigurationModel roomConfigurationModel = new RoomConfigurationModel();
        roomConfigurationModel.setRoomName(roomName);
        roomConfigurationModel.setMain(main);
        roomConfigurationDao.save(roomConfigurationModel);
        simpMessagingTemplate.convertAndSend("/rooms/rooms", roomConfigurationDao.findAll());

    }


    public void deleteRoom(int roomID) {
        List<DeviceConfigurationModel> devices;
        devices = deviceConfigurationDao.findByRoomID(roomID);
        devices.forEach(device -> {
            int serial = device.getSerial();
            deviceConfigurationDao.deleteBySerial(serial);
            System.out.println("deleted device with serial:" + serial);
            simpMessagingTemplate.convertAndSend("/device/device/" + serial, new SimpleResponse("doesnt exists"));
        });
        roomConfigurationDao.deleteById(roomID);
        simpMessagingTemplate.convertAndSend("/rooms/rooms", roomConfigurationDao.findAll());
    }


    public void editName(String roomName, int id) throws Exception {
        roomConfigurationDao.findById(id).map(deviceConfigurationModel -> {
                    deviceConfigurationModel.setRoomName(roomName);
                    return roomConfigurationDao.save(deviceConfigurationModel);
                }
        ).orElseThrow(
                Exception::new
        );
        System.out.println("Rename room with id:" + id);
        simpMessagingTemplate.convertAndSend("/rooms/rooms", roomConfigurationDao.findAll());
    }

}
