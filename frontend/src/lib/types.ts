export type Seat = {
    id: string;
    seatNumber: string;
    isOccupied: boolean;
    isWindow: boolean;
    isLegroom: boolean;
    isNearExit: boolean;
};

export type Flight = {
    id: string;
    from: string;
    to: string;
    departureTime: Date;
    price: number;
    planeModel: string;
    exitRows: number[];
    seats: Seat[];
};