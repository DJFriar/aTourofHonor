package net.tommyc.android.tourofhonor;

import android.provider.BaseColumns;

public class dbContract {

    public static final class BonusDataDB implements BaseColumns {

        public static final String TABLE_NAME = "BonusData";
        public static final String COLUMN_CODE = "sBonusCode";
        public static final String COLUMN_NAME = "sBonusName";
        public static final String COLUMN_ADDRESS = "sAddress";
        public static final String COLUMN_CITY = "sCity";
        public static final String COLUMN_STATE = "sState";
        public static final String COLUMN_GPS = "sGPS";
        public static final String COLUMN_ACCESS = "sAccess";
        public static final String COLUMN_FLAVOR = "sFlavorText";
        public static final String COLUMN_MADEINAMERICA = "sMadeInAmericaText";
        public static final String COLUMN_IMAGE = "sImageName";
    }
}
