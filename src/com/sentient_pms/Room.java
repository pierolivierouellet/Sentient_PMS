package com.sentient_pms;

public class Room {

    private int roomNumber;
    private RoomType roomType;
    private RoomStatus roomStatus;

    public Room(int roomNumber, RoomType roomType) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.roomStatus = RoomStatus.AVAILABLE;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public RoomStatus getRoomStatus() {
        return roomStatus;
    }

    public boolean isAvailable() {
        return roomStatus == RoomStatus.AVAILABLE;
    }

    public void bookRoom() {
        if (roomStatus == RoomStatus.AVAILABLE) {
            roomStatus = RoomStatus.OCCUPIED;
        }
    }

    public void checkOut() {
        if (roomStatus == RoomStatus.OCCUPIED) {
            roomStatus = RoomStatus.DIRTY;
        }
    }

    public void cleanRoom() {
        if (roomStatus == RoomStatus.DIRTY) {
            roomStatus = RoomStatus.AVAILABLE;
        }
    }

    public void setOutOfOrder() {
        roomStatus = RoomStatus.OUT_OF_ORDER;
    }

    @Override
    public String toString() {
        return "Room " + roomNumber +
                " | Type: " + roomType +
                " | Status: " + roomStatus;
    }
}