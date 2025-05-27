package com.swe.nmb_map.utils;

public enum CoordinateEnum {
    G1(1273, 661),
    F1(593, 3313),
    E1(597, 5849),
    D1(3681, 6029),
    C1(6680, 5825),
    B1(6760, 3237),
    A1(6164, 653),

    G2(1135, 621),
    F2(537, 3065),
    E2(555, 5749),
    D2(3891, 5663),
    C2(6887, 5723),
    B2(7033, 3093),
    A2(6297, 649),

    G3(1645, 705),
    F3(777, 3219),
    E3(773, 6105),
    D3(4953, 6001),
    C3(8772, 6149),
    B3(8636, 3299),
    A3(7972, 745),

    G4(1413, 665),
    F4(721, 3121),
    E4(769, 5883),
    D4(4721, 5863),
    C4(8384, 5851),
    B4(8280, 3040),
    A4(7684, 589),

    G5(1285, 637),
    F5(615, 3091),
    E5(607, 5859),
    D5(4209, 5797),
    C5(7572, 5823),
    B5(7526, 3099),
    A5(6836, 613);

    private final int x;
    private final int y;

    CoordinateEnum(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return name() + ": (" + x + ", " + y + ")";
    }
}
