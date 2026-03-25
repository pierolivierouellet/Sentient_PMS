package com.sentient_pms.repository;

import com.sentient_pms.model.Room;
import com.sentient_pms.model.RoomStatus;
import com.sentient_pms.model.RoomType;

import java.util.ArrayList;
import java.util.List;

public class RoomRepository {

    private final List<Room> rooms = new ArrayList<>();

    public RoomRepository() {
        initializeRooms();
    }

    private void initializeRooms() {
        for (int i = 101; i <= 130; i++) {
            rooms.add(new Room(i, RoomType.STANDARD));
        }
        System.out.println("Hotel initialized with 30 rooms.");
    }

    public void cleanRoomsRange(int start, int end) {
        for (int i = start; i <= end; i++) {
            Room room = findRoomByNumber(i);
            if (room != null && room.getRoomStatus() == RoomStatus.DIRTY) {
                room.cleanRoom();
            }
        }
    }

    public Room findRoomByNumber(int roomNumber) {
        for (Room room : rooms) {
            if (room.getRoomNumber() == roomNumber) {
                return room;
            }
        }
        return null;
    }

    public void viewAllRooms() {
        for (Room room : rooms) {
            System.out.println(room);
        }
    }

    // ✅ REQUIRED FOR UI
    public List<Room> getRooms() {
        return rooms;
    }


    public void setRoomStatus(int roomNumber, RoomStatus status) {
        Room room = findRoomByNumber(roomNumber);
        if (room != null) {
            room.setRoomStatus(status);
        }
    }

}