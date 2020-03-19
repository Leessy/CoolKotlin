package com.aiface.jidacard.card;

/**
 * @author Created by 刘承. on 2019/12/13
 * <p>
 * --深圳市尚美欣辰科技有限公司.
 */
public class CardParse {

    //国家
    public static String parseCountry(String code) {
        short var4 = -1;
        switch (code.hashCode()) {
            case 64598:
                if (code.equals("ABW")) {
                    var4 = 13;
                }
                break;
            case 64706:
                if (code.equals("AFG")) {
                    var4 = 2;
                }
                break;
            case 64745:
                if (code.equals("AGO")) {
                    var4 = 7;
                }
                break;
            case 64793:
                if (code.equals("AIA")) {
                    var4 = 4;
                }
                break;
            case 64886:
                if (code.equals("ALA")) {
                    var4 = 14;
                }
                break;
            case 64887:
                if (code.equals("ALB")) {
                    var4 = 5;
                }
                break;
            case 64951:
                if (code.equals("AND")) {
                    var4 = 0;
                }
                break;
            case 65076:
                if (code.equals("ARE")) {
                    var4 = 1;
                }
                break;
            case 65078:
                if (code.equals("ARG")) {
                    var4 = 9;
                }
                break;
            case 65084:
                if (code.equals("ARM")) {
                    var4 = 6;
                }
                break;
            case 65115:
                if (code.equals("ASM")) {
                    var4 = 10;
                }
                break;
            case 65134:
                if (code.equals("ATA")) {
                    var4 = 8;
                }
                break;
            case 65139:
                if (code.equals("ATF")) {
                    var4 = 220;
                }
                break;
            case 65140:
                if (code.equals("ATG")) {
                    var4 = 3;
                }
                break;
            case 65183:
                if (code.equals("AUS")) {
                    var4 = 12;
                }
                break;
            case 65184:
                if (code.equals("AUT")) {
                    var4 = 11;
                }
                break;
            case 65324:
                if (code.equals("AZE")) {
                    var4 = 15;
                }
                break;
            case 65607:
                if (code.equals("BDI")) {
                    var4 = 23;
                }
                break;
            case 65641:
                if (code.equals("BEL")) {
                    var4 = 19;
                }
                break;
            case 65643:
                if (code.equals("BEN")) {
                    var4 = 24;
                }
                break;
            case 65648:
                if (code.equals("BES")) {
                    var4 = 29;
                }
                break;
            case 65661:
                if (code.equals("BFA")) {
                    var4 = 20;
                }
                break;
            case 65695:
                if (code.equals("BGD")) {
                    var4 = 18;
                }
                break;
            case 65709:
                if (code.equals("BGR")) {
                    var4 = 21;
                }
                break;
            case 65740:
                if (code.equals("BHR")) {
                    var4 = 22;
                }
                break;
            case 65741:
                if (code.equals("BHS")) {
                    var4 = 31;
                }
                break;
            case 65761:
                if (code.equals("BIH")) {
                    var4 = 16;
                }
                break;
            case 65859:
                if (code.equals("BLM")) {
                    var4 = 25;
                }
                break;
            case 65864:
                if (code.equals("BLR")) {
                    var4 = 35;
                }
                break;
            case 65872:
                if (code.equals("BLZ")) {
                    var4 = 36;
                }
                break;
            case 65898:
                if (code.equals("BMU")) {
                    var4 = 26;
                }
                break;
            case 65951:
                if (code.equals("BOL")) {
                    var4 = 28;
                }
                break;
            case 66033:
                if (code.equals("BRA")) {
                    var4 = 30;
                }
                break;
            case 66034:
                if (code.equals("BRB")) {
                    var4 = 17;
                }
                break;
            case 66046:
                if (code.equals("BRN")) {
                    var4 = 27;
                }
                break;
            case 66108:
                if (code.equals("BTN")) {
                    var4 = 32;
                }
                break;
            case 66176:
                if (code.equals("BVT")) {
                    var4 = 33;
                }
                break;
            case 66188:
                if (code.equals("BWA")) {
                    var4 = 34;
                }
                break;
            case 66472:
                if (code.equals("CAF")) {
                    var4 = 39;
                }
                break;
            case 66480:
                if (code.equals("CAN")) {
                    var4 = 37;
                }
                break;
            case 66539:
                if (code.equals("CCK")) {
                    var4 = 38;
                }
                break;
            case 66688:
                if (code.equals("CHE")) {
                    var4 = 40;
                }
                break;
            case 66695:
                if (code.equals("CHL")) {
                    var4 = 41;
                }
                break;
            case 66697:
                if (code.equals("CHN")) {
                    var4 = 210;
                }
                break;
            case 66736:
                if (code.equals("CIV")) {
                    var4 = 244;
                }
                break;
            case 66856:
                if (code.equals("CMR")) {
                    var4 = 42;
                }
                break;
            case 66904:
                if (code.equals("COD")) {
                    var4 = 212;
                }
                break;
            case 66907:
                if (code.equals("COG")) {
                    var4 = 211;
                }
                break;
            case 66911:
                if (code.equals("COK")) {
                    var4 = 223;
                }
                break;
            case 66912:
                if (code.equals("COL")) {
                    var4 = 43;
                }
                break;
            case 66913:
                if (code.equals("COM")) {
                    var4 = 106;
                }
                break;
            case 66953:
                if (code.equals("CPV")) {
                    var4 = 46;
                }
                break;
            case 67002:
                if (code.equals("CRI")) {
                    var4 = 44;
                }
                break;
            case 67088:
                if (code.equals("CUB")) {
                    var4 = 45;
                }
                break;
            case 67197:
                if (code.equals("CXR")) {
                    var4 = 47;
                }
                break;
            case 67223:
                if (code.equals("CYM")) {
                    var4 = 108;
                }
                break;
            case 67226:
                if (code.equals("CYP")) {
                    var4 = 48;
                }
                break;
            case 67246:
                if (code.equals("CZE")) {
                    var4 = 49;
                }
                break;
            case 67572:
                if (code.equals("DEU")) {
                    var4 = 50;
                }
                break;
            case 67715:
                if (code.equals("DJI")) {
                    var4 = 51;
                }
                break;
            case 67800:
                if (code.equals("DMA")) {
                    var4 = 53;
                }
                break;
            case 67841:
                if (code.equals("DNK")) {
                    var4 = 52;
                }
                break;
            case 67874:
                if (code.equals("DOM")) {
                    var4 = 54;
                }
                break;
            case 68203:
                if (code.equals("DZA")) {
                    var4 = 55;
                }
                break;
            case 68471:
                if (code.equals("ECU")) {
                    var4 = 56;
                }
                break;
            case 68599:
                if (code.equals("EGY")) {
                    var4 = 58;
                }
                break;
            case 68924:
                if (code.equals("ERI")) {
                    var4 = 60;
                }
                break;
            case 68954:
                if (code.equals("ESH")) {
                    var4 = 59;
                }
                break;
            case 68962:
                if (code.equals("ESP")) {
                    var4 = 61;
                }
                break;
            case 68966:
                if (code.equals("EST")) {
                    var4 = 57;
                }
                break;
            case 68985:
                if (code.equals("ETH")) {
                    var4 = 217;
                }
                break;
            case 69611:
                if (code.equals("FIN")) {
                    var4 = 62;
                }
                break;
            case 69637:
                if (code.equals("FJI")) {
                    var4 = 63;
                }
                break;
            case 69701:
                if (code.equals("FLK")) {
                    var4 = 64;
                }
                break;
            case 69877:
                if (code.equals("FRA")) {
                    var4 = 67;
                }
                break;
            case 69891:
                if (code.equals("FRO")) {
                    var4 = 66;
                }
                break;
            case 69920:
                if (code.equals("FSM")) {
                    var4 = 65;
                }
                break;
            case 70312:
                if (code.equals("GAB")) {
                    var4 = 68;
                }
                break;
            case 70359:
                if (code.equals("GBR")) {
                    var4 = 224;
                }
                break;
            case 70449:
                if (code.equals("GEO")) {
                    var4 = 70;
                }
                break;
            case 70521:
                if (code.equals("GGY")) {
                    var4 = 214;
                }
                break;
            case 70528:
                if (code.equals("GHA")) {
                    var4 = 72;
                }
                break;
            case 70560:
                if (code.equals("GIB")) {
                    var4 = 73;
                }
                break;
            case 70572:
                if (code.equals("GIN")) {
                    var4 = 75;
                }
                break;
            case 70667:
                if (code.equals("GLP")) {
                    var4 = 76;
                }
                break;
            case 70684:
                if (code.equals("GMB")) {
                    var4 = 215;
                }
                break;
            case 70715:
                if (code.equals("GNB")) {
                    var4 = 82;
                }
                break;
            case 70730:
                if (code.equals("GNQ")) {
                    var4 = 77;
                }
                break;
            case 70840:
                if (code.equals("GRC")) {
                    var4 = 78;
                }
                break;
            case 70841:
                if (code.equals("GRD")) {
                    var4 = 69;
                }
                break;
            case 70849:
                if (code.equals("GRL")) {
                    var4 = 74;
                }
                break;
            case 70912:
                if (code.equals("GTM")) {
                    var4 = 80;
                }
                break;
            case 70936:
                if (code.equals("GUF")) {
                    var4 = 71;
                }
                break;
            case 70943:
                if (code.equals("GUM")) {
                    var4 = 81;
                }
                break;
            case 70955:
                if (code.equals("GUY")) {
                    var4 = 83;
                }
                break;
            case 71588:
                if (code.equals("HKG")) {
                    var4 = 84;
                }
                break;
            case 71647:
                if (code.equals("HMD")) {
                    var4 = 85;
                }
                break;
            case 71678:
                if (code.equals("HND")) {
                    var4 = 86;
                }
                break;
            case 71820:
                if (code.equals("HRV")) {
                    var4 = 87;
                }
                break;
            case 71869:
                if (code.equals("HTI")) {
                    var4 = 88;
                }
                break;
            case 71905:
                if (code.equals("HUN")) {
                    var4 = 89;
                }
                break;
            case 72339:
                if (code.equals("IDN")) {
                    var4 = 90;
                }
                break;
            case 72618:
                if (code.equals("IMN")) {
                    var4 = 93;
                }
                break;
            case 72639:
                if (code.equals("IND")) {
                    var4 = 94;
                }
                break;
            case 72686:
                if (code.equals("IOT")) {
                    var4 = 95;
                }
                break;
            case 72771:
                if (code.equals("IRL")) {
                    var4 = 91;
                }
                break;
            case 72773:
                if (code.equals("IRN")) {
                    var4 = 97;
                }
                break;
            case 72776:
                if (code.equals("IRQ")) {
                    var4 = 96;
                }
                break;
            case 72802:
                if (code.equals("ISL")) {
                    var4 = 98;
                }
                break;
            case 72808:
                if (code.equals("ISR")) {
                    var4 = 92;
                }
                break;
            case 72822:
                if (code.equals("ITA")) {
                    var4 = 99;
                }
                break;
            case 73206:
                if (code.equals("JAM")) {
                    var4 = 101;
                }
                break;
            case 73342:
                if (code.equals("JEY")) {
                    var4 = 100;
                }
                break;
            case 73645:
                if (code.equals("JOR")) {
                    var4 = 102;
                }
                break;
            case 73672:
                if (code.equals("JPN")) {
                    var4 = 103;
                }
                break;
            case 74180:
                if (code.equals("KAZ")) {
                    var4 = 235;
                }
                break;
            case 74292:
                if (code.equals("KEN")) {
                    var4 = 245;
                }
                break;
            case 74366:
                if (code.equals("KGZ")) {
                    var4 = 234;
                }
                break;
            case 74384:
                if (code.equals("KHM")) {
                    var4 = 104;
                }
                break;
            case 74420:
                if (code.equals("KIR")) {
                    var4 = 105;
                }
                break;
            case 74558:
                if (code.equals("KNA")) {
                    var4 = 239;
                }
                break;
            case 74606:
                if (code.equals("KOR")) {
                    var4 = 232;
                }
                break;
            case 74856:
                if (code.equals("KWT")) {
                    var4 = 107;
                }
                break;
            case 75130:
                if (code.equals("LAO")) {
                    var4 = 230;
                }
                break;
            case 75160:
                if (code.equals("LBN")) {
                    var4 = 109;
                }
                break;
            case 75164:
                if (code.equals("LBR")) {
                    var4 = 112;
                }
                break;
            case 75171:
                if (code.equals("LBY")) {
                    var4 = 117;
                }
                break;
            case 75178:
                if (code.equals("LCA")) {
                    var4 = 242;
                }
                break;
            case 75368:
                if (code.equals("LIE")) {
                    var4 = 110;
                }
                break;
            case 75426:
                if (code.equals("LKA")) {
                    var4 = 111;
                }
                break;
            case 75688:
                if (code.equals("LSO")) {
                    var4 = 113;
                }
                break;
            case 75725:
                if (code.equals("LTU")) {
                    var4 = 114;
                }
                break;
            case 75759:
                if (code.equals("LUX")) {
                    var4 = 115;
                }
                break;
            case 75767:
                if (code.equals("LVA")) {
                    var4 = 116;
                }
                break;
            case 76079:
                if (code.equals("MAC")) {
                    var4 = 128;
                }
                break;
            case 76082:
                if (code.equals("MAF")) {
                    var4 = 122;
                }
                break;
            case 76094:
                if (code.equals("MAR")) {
                    var4 = 118;
                }
                break;
            case 76153:
                if (code.equals("MCO")) {
                    var4 = 119;
                }
                break;
            case 76170:
                if (code.equals("MDA")) {
                    var4 = 120;
                }
                break;
            case 76176:
                if (code.equals("MDG")) {
                    var4 = 123;
                }
                break;
            case 76191:
                if (code.equals("MDV")) {
                    var4 = 133;
                }
                break;
            case 76224:
                if (code.equals("MEX")) {
                    var4 = 135;
                }
                break;
            case 76305:
                if (code.equals("MHL")) {
                    var4 = 124;
                }
                break;
            case 76390:
                if (code.equals("MKD")) {
                    var4 = 125;
                }
                break;
            case 76426:
                if (code.equals("MLI")) {
                    var4 = 126;
                }
                break;
            case 76437:
                if (code.equals("MLT")) {
                    var4 = 132;
                }
                break;
            case 76466:
                if (code.equals("MMR")) {
                    var4 = 127;
                }
                break;
            case 76484:
                if (code.equals("MNE")) {
                    var4 = 121;
                }
                break;
            case 76486:
                if (code.equals("MNG")) {
                    var4 = 246;
                }
                break;
            case 76495:
                if (code.equals("MNP")) {
                    var4 = 216;
                }
                break;
            case 76536:
                if (code.equals("MOZ")) {
                    var4 = 213;
                }
                break;
            case 76623:
                if (code.equals("MRT")) {
                    var4 = 130;
                }
                break;
            case 76652:
                if (code.equals("MSR")) {
                    var4 = 131;
                }
                break;
            case 76682:
                if (code.equals("MTQ")) {
                    var4 = 129;
                }
                break;
            case 76715:
                if (code.equals("MUS")) {
                    var4 = 243;
                }
                break;
            case 76767:
                if (code.equals("MWI")) {
                    var4 = 134;
                }
                break;
            case 76839:
                if (code.equals("MYS")) {
                    var4 = 136;
                }
                break;
            case 76840:
                if (code.equals("MYT")) {
                    var4 = 206;
                }
                break;
            case 77050:
                if (code.equals("NAM")) {
                    var4 = 137;
                }
                break;
            case 77111:
                if (code.equals("NCL")) {
                    var4 = 218;
                }
                break;
            case 77179:
                if (code.equals("NER")) {
                    var4 = 138;
                }
                break;
            case 77203:
                if (code.equals("NFK")) {
                    var4 = 139;
                }
                break;
            case 77224:
                if (code.equals("NGA")) {
                    var4 = 140;
                }
                break;
            case 77288:
                if (code.equals("NIC")) {
                    var4 = 141;
                }
                break;
            case 77306:
                if (code.equals("NIU")) {
                    var4 = 221;
                }
                break;
            case 77382:
                if (code.equals("NLD")) {
                    var4 = 142;
                }
                break;
            case 77489:
                if (code.equals("NOR")) {
                    var4 = 143;
                }
                break;
            case 77514:
                if (code.equals("NPL")) {
                    var4 = 144;
                }
                break;
            case 77585:
                if (code.equals("NRU")) {
                    var4 = 145;
                }
                break;
            case 77824:
                if (code.equals("NZL")) {
                    var4 = 228;
                }
                break;
            case 78384:
                if (code.equals("OMN")) {
                    var4 = 146;
                }
                break;
            case 78970:
                if (code.equals("PAK")) {
                    var4 = 152;
                }
                break;
            case 78973:
                if (code.equals("PAN")) {
                    var4 = 147;
                }
                break;
            case 79035:
                if (code.equals("PCN")) {
                    var4 = 154;
                }
                break;
            case 79101:
                if (code.equals("PER")) {
                    var4 = 148;
                }
                break;
            case 79188:
                if (code.equals("PHL")) {
                    var4 = 151;
                }
                break;
            case 79323:
                if (code.equals("PLW")) {
                    var4 = 157;
                }
                break;
            case 79369:
                if (code.equals("PNG")) {
                    var4 = 150;
                }
                break;
            case 79405:
                if (code.equals("POL")) {
                    var4 = 153;
                }
                break;
            case 79495:
                if (code.equals("PRI")) {
                    var4 = 155;
                }
                break;
            case 79497:
                if (code.equals("PRK")) {
                    var4 = 231;
                }
                break;
            case 79506:
                if (code.equals("PRT")) {
                    var4 = 233;
                }
                break;
            case 79511:
                if (code.equals("PRY")) {
                    var4 = 158;
                }
                break;
            case 79522:
                if (code.equals("PSE")) {
                    var4 = 156;
                }
                break;
            case 79709:
                if (code.equals("PYF")) {
                    var4 = 149;
                }
                break;
            case 79940:
                if (code.equals("QAT")) {
                    var4 = 159;
                }
                break;
            case 81026:
                if (code.equals("REU")) {
                    var4 = 160;
                }
                break;
            case 81336:
                if (code.equals("ROU")) {
                    var4 = 161;
                }
                break;
            case 81520:
                if (code.equals("RUS")) {
                    var4 = 163;
                }
                break;
            case 81564:
                if (code.equals("RWA")) {
                    var4 = 164;
                }
                break;
            case 81863:
                if (code.equals("SAU")) {
                    var4 = 229;
                }
                break;
            case 81949:
                if (code.equals("SDN")) {
                    var4 = 167;
                }
                break;
            case 81980:
                if (code.equals("SEN")) {
                    var4 = 175;
                }
                break;
            case 82044:
                if (code.equals("SGP")) {
                    var4 = 169;
                }
                break;
            case 82047:
                if (code.equals("SGS")) {
                    var4 = 79;
                }
                break;
            case 82073:
                if (code.equals("SHN")) {
                    var4 = 241;
                }
                break;
            case 82134:
                if (code.equals("SJM")) {
                    var4 = 171;
                }
                break;
            case 82185:
                if (code.equals("SLB")) {
                    var4 = 165;
                }
                break;
            case 82188:
                if (code.equals("SLE")) {
                    var4 = 173;
                }
                break;
            case 82205:
                if (code.equals("SLV")) {
                    var4 = 180;
                }
                break;
            case 82232:
                if (code.equals("SMR")) {
                    var4 = 174;
                }
                break;
            case 82289:
                if (code.equals("SOM")) {
                    var4 = 176;
                }
                break;
            case 82320:
                if (code.equals("SPM")) {
                    var4 = 240;
                }
                break;
            case 82371:
                if (code.equals("SRB")) {
                    var4 = 162;
                }
                break;
            case 82404:
                if (code.equals("SSD")) {
                    var4 = 178;
                }
                break;
            case 82447:
                if (code.equals("STP")) {
                    var4 = 179;
                }
                break;
            case 82480:
                if (code.equals("SUR")) {
                    var4 = 177;
                }
                break;
            case 82504:
                if (code.equals("SVK")) {
                    var4 = 172;
                }
                break;
            case 82507:
                if (code.equals("SVN")) {
                    var4 = 170;
                }
                break;
            case 82529:
                if (code.equals("SWE")) {
                    var4 = 168;
                }
                break;
            case 82550:
                if (code.equals("SWZ")) {
                    var4 = 182;
                }
                break;
            case 82589:
                if (code.equals("SYC")) {
                    var4 = 166;
                }
                break;
            case 82604:
                if (code.equals("SYR")) {
                    var4 = 181;
                }
                break;
            case 82866:
                if (code.equals("TCA")) {
                    var4 = 183;
                }
                break;
            case 82869:
                if (code.equals("TCD")) {
                    var4 = 184;
                }
                break;
            case 83004:
                if (code.equals("TGO")) {
                    var4 = 185;
                }
                break;
            case 83021:
                if (code.equals("THA")) {
                    var4 = 186;
                }
                break;
            case 83093:
                if (code.equals("TJK")) {
                    var4 = 236;
                }
                break;
            case 83125:
                if (code.equals("TKL")) {
                    var4 = 187;
                }
                break;
            case 83126:
                if (code.equals("TKM")) {
                    var4 = 237;
                }
                break;
            case 83163:
                if (code.equals("TLS")) {
                    var4 = 188;
                }
                break;
            case 83251:
                if (code.equals("TON")) {
                    var4 = 190;
                }
                break;
            case 83407:
                if (code.equals("TTO")) {
                    var4 = 225;
                }
                break;
            case 83437:
                if (code.equals("TUN")) {
                    var4 = 189;
                }
                break;
            case 83441:
                if (code.equals("TUR")) {
                    var4 = 191;
                }
                break;
            case 83445:
                if (code.equals("TUV")) {
                    var4 = 192;
                }
                break;
            case 83499:
                if (code.equals("TWN")) {
                    var4 = 227;
                }
                break;
            case 83579:
                if (code.equals("TZA")) {
                    var4 = 193;
                }
                break;
            case 83951:
                if (code.equals("UGA")) {
                    var4 = 195;
                }
                break;
            case 84092:
                if (code.equals("UKR")) {
                    var4 = 194;
                }
                break;
            case 84145:
                if (code.equals("UMI")) {
                    var4 = 222;
                }
                break;
            case 84316:
                if (code.equals("URY")) {
                    var4 = 197;
                }
                break;
            case 84323:
                if (code.equals("USA")) {
                    var4 = 196;
                }
                break;
            case 84541:
                if (code.equals("UZB")) {
                    var4 = 238;
                }
                break;
            case 84745:
                if (code.equals("VAT")) {
                    var4 = 198;
                }
                break;
            case 84807:
                if (code.equals("VCT")) {
                    var4 = 226;
                }
                break;
            case 84863:
                if (code.equals("VEN")) {
                    var4 = 199;
                }
                break;
            case 84913:
                if (code.equals("VGB")) {
                    var4 = 200;
                }
                break;
            case 84991:
                if (code.equals("VIR")) {
                    var4 = 201;
                }
                break;
            case 85141:
                if (code.equals("VNM")) {
                    var4 = 202;
                }
                break;
            case 85365:
                if (code.equals("VUT")) {
                    var4 = 219;
                }
                break;
            case 86033:
                if (code.equals("WLF")) {
                    var4 = 203;
                }
                break;
            case 86257:
                if (code.equals("WSM")) {
                    var4 = 204;
                }
                break;
            case 87745:
                if (code.equals("YEM")) {
                    var4 = 205;
                }
                break;
            case 88575:
                if (code.equals("ZAF")) {
                    var4 = 207;
                }
                break;
            case 88943:
                if (code.equals("ZMB")) {
                    var4 = 208;
                }
                break;
            case 89256:
                if (code.equals("ZWE")) {
                    var4 = 209;
                }
        }

        String Country;
        switch (var4) {
            case 0:
                Country = "安道尔";
                break;
            case 1:
                Country = "阿联酋";
                break;
            case 2:
                Country = "阿富汗";
                break;
            case 3:
                Country = "安提瓜和巴布达";
                break;
            case 4:
                Country = "安圭拉";
                break;
            case 5:
                Country = "阿尔巴尼亚";
                break;
            case 6:
                Country = "亚美尼亚";
                break;
            case 7:
                Country = "安哥拉";
                break;
            case 8:
                Country = "南极洲";
                break;
            case 9:
                Country = "阿根廷";
                break;
            case 10:
                Country = "美属萨摩亚";
                break;
            case 11:
                Country = "奥地利";
                break;
            case 12:
                Country = "澳大利亚";
                break;
            case 13:
                Country = "阿鲁巴";
                break;
            case 14:
                Country = "奥兰群岛";
                break;
            case 15:
                Country = "阿塞拜疆";
                break;
            case 16:
                Country = "波黑";
                break;
            case 17:
                Country = "巴巴多斯";
                break;
            case 18:
                Country = "孟加拉";
                break;
            case 19:
                Country = "比利时";
                break;
            case 20:
                Country = "布基纳法索";
                break;
            case 21:
                Country = "保加利亚";
                break;
            case 22:
                Country = "巴林";
                break;
            case 23:
                Country = "布隆迪";
                break;
            case 24:
                Country = "贝宁";
                break;
            case 25:
                Country = "圣巴泰勒米岛";
                break;
            case 26:
                Country = "百慕大";
                break;
            case 27:
                Country = "文莱";
                break;
            case 28:
                Country = "玻利维亚";
                break;
            case 29:
                Country = "荷兰加勒比区";
                break;
            case 30:
                Country = "巴西";
                break;
            case 31:
                Country = "巴哈马";
                break;
            case 32:
                Country = "不丹";
                break;
            case 33:
                Country = "布韦岛";
                break;
            case 34:
                Country = "博茨瓦纳";
                break;
            case 35:
                Country = "白俄罗斯";
                break;
            case 36:
                Country = "伯利兹";
                break;
            case 37:
                Country = "加拿大";
                break;
            case 38:
                Country = "科科斯群岛";
                break;
            case 39:
                Country = "中非";
                break;
            case 40:
                Country = "瑞士";
                break;
            case 41:
                Country = "智利";
                break;
            case 42:
                Country = "喀麦隆";
                break;
            case 43:
                Country = "哥伦比亚";
                break;
            case 44:
                Country = "哥斯达黎加";
                break;
            case 45:
                Country = "古巴";
                break;
            case 46:
                Country = "佛得角";
                break;
            case 47:
                Country = "圣诞岛";
                break;
            case 48:
                Country = "塞浦路斯";
                break;
            case 49:
                Country = "捷克";
                break;
            case 50:
                Country = "德国";
                break;
            case 51:
                Country = "吉布提";
                break;
            case 52:
                Country = "丹麦";
                break;
            case 53:
                Country = "多米尼克";
                break;
            case 54:
                Country = "多米尼加";
                break;
            case 55:
                Country = "阿尔及利亚";
                break;
            case 56:
                Country = "厄瓜多尔";
                break;
            case 57:
                Country = "爱沙尼亚";
                break;
            case 58:
                Country = "埃及";
                break;
            case 59:
                Country = "西撒哈拉";
                break;
            case 60:
                Country = "厄立特里亚";
                break;
            case 61:
                Country = "西班牙";
                break;
            case 62:
                Country = "芬兰";
                break;
            case 63:
                Country = "斐济群岛";
                break;
            case 64:
                Country = "马尔维纳斯群岛（ 福克兰）";
                break;
            case 65:
                Country = "密克罗尼西亚联邦";
                break;
            case 66:
                Country = "法罗群岛";
                break;
            case 67:
                Country = "法国";
                break;
            case 68:
                Country = "加蓬";
                break;
            case 69:
                Country = "格林纳达";
                break;
            case 70:
                Country = "格鲁吉亚";
                break;
            case 71:
                Country = "法属圭亚那";
                break;
            case 72:
                Country = "加纳";
                break;
            case 73:
                Country = "直布罗陀";
                break;
            case 74:
                Country = "格陵兰";
                break;
            case 75:
                Country = "几内亚";
                break;
            case 76:
                Country = "瓜德罗普";
                break;
            case 77:
                Country = "赤道几内亚";
                break;
            case 78:
                Country = "希腊";
                break;
            case 79:
                Country = "南乔治亚岛和南桑威奇群岛";
                break;
            case 80:
                Country = "危地马拉";
                break;
            case 81:
                Country = "关岛";
                break;
            case 82:
                Country = "几内亚比绍";
                break;
            case 83:
                Country = "圭亚那";
                break;
            case 84:
                Country = "香港";
                break;
            case 85:
                Country = "赫德岛和麦克唐纳群岛";
                break;
            case 86:
                Country = "洪都拉斯";
                break;
            case 87:
                Country = "克罗地亚";
                break;
            case 88:
                Country = "海地";
                break;
            case 89:
                Country = "匈牙利";
                break;
            case 90:
                Country = "印尼";
                break;
            case 91:
                Country = "爱尔兰";
                break;
            case 92:
                Country = "以色列";
                break;
            case 93:
                Country = "马恩岛";
                break;
            case 94:
                Country = "印度";
                break;
            case 95:
                Country = "英属印度洋领地";
                break;
            case 96:
                Country = "伊拉克";
                break;
            case 97:
                Country = "伊朗";
                break;
            case 98:
                Country = "冰岛";
                break;
            case 99:
                Country = "意大利";
                break;
            case 100:
                Country = "泽西岛";
                break;
            case 101:
                Country = "牙买加";
                break;
            case 102:
                Country = "约旦";
                break;
            case 103:
                Country = "日本";
                break;
            case 104:
                Country = "柬埔寨";
                break;
            case 105:
                Country = "基里巴斯";
                break;
            case 106:
                Country = "科摩罗";
                break;
            case 107:
                Country = "科威特";
                break;
            case 108:
                Country = "开曼群岛";
                break;
            case 109:
                Country = "黎巴嫩";
                break;
            case 110:
                Country = "列支敦士登";
                break;
            case 111:
                Country = "斯里兰卡";
                break;
            case 112:
                Country = "利比里亚";
                break;
            case 113:
                Country = "莱索托";
                break;
            case 114:
                Country = "立陶宛";
                break;
            case 115:
                Country = "卢森堡";
                break;
            case 116:
                Country = "拉脱维亚";
                break;
            case 117:
                Country = "利比亚";
                break;
            case 118:
                Country = "摩洛哥";
                break;
            case 119:
                Country = "摩纳哥";
                break;
            case 120:
                Country = "摩尔多瓦";
                break;
            case 121:
                Country = "黑山";
                break;
            case 122:
                Country = "法属圣马丁";
                break;
            case 123:
                Country = "马达加斯加";
                break;
            case 124:
                Country = "马绍尔群岛";
                break;
            case 125:
                Country = "马其顿";
                break;
            case 126:
                Country = "马里";
                break;
            case 127:
                Country = "缅甸";
                break;
            case 128:
                Country = "澳门";
                break;
            case 129:
                Country = "马提尼克";
                break;
            case 130:
                Country = "毛里塔尼亚";
                break;
            case 131:
                Country = "蒙塞拉特岛";
                break;
            case 132:
                Country = "马耳他";
                break;
            case 133:
                Country = "马尔代夫";
                break;
            case 134:
                Country = "马拉维";
                break;
            case 135:
                Country = "墨西哥";
                break;
            case 136:
                Country = "马来西亚";
                break;
            case 137:
                Country = "纳米比亚";
                break;
            case 138:
                Country = "尼日尔";
                break;
            case 139:
                Country = "诺福克岛";
                break;
            case 140:
                Country = "尼日利亚";
                break;
            case 141:
                Country = "尼加拉瓜";
                break;
            case 142:
                Country = "荷兰";
                break;
            case 143:
                Country = "挪威";
                break;
            case 144:
                Country = "尼泊尔";
                break;
            case 145:
                Country = "瑙鲁";
                break;
            case 146:
                Country = "阿曼";
                break;
            case 147:
                Country = "巴拿马";
                break;
            case 148:
                Country = "秘鲁";
                break;
            case 149:
                Country = "法属波利尼西亚";
                break;
            case 150:
                Country = "巴布亚新几内亚";
                break;
            case 151:
                Country = "菲律宾";
                break;
            case 152:
                Country = "巴基斯坦";
                break;
            case 153:
                Country = "波兰";
                break;
            case 154:
                Country = "皮特凯恩群岛";
                break;
            case 155:
                Country = "波多黎各";
                break;
            case 156:
                Country = "巴勒斯坦";
                break;
            case 157:
                Country = "帕劳";
                break;
            case 158:
                Country = "巴拉圭";
                break;
            case 159:
                Country = "卡塔尔";
                break;
            case 160:
                Country = "留尼汪";
                break;
            case 161:
                Country = "罗马尼亚";
                break;
            case 162:
                Country = "塞尔维亚";
                break;
            case 163:
                Country = "俄罗斯";
                break;
            case 164:
                Country = "卢旺达";
                break;
            case 165:
                Country = "所罗门群岛";
                break;
            case 166:
                Country = "塞舌尔";
                break;
            case 167:
                Country = "苏丹";
                break;
            case 168:
                Country = "瑞典";
                break;
            case 169:
                Country = "新加坡";
                break;
            case 170:
                Country = "斯洛文尼亚";
                break;
            case 171:
                Country = "斯瓦尔巴群岛和扬马延岛";
                break;
            case 172:
                Country = "斯洛伐克";
                break;
            case 173:
                Country = "塞拉利昂";
                break;
            case 174:
                Country = "圣马力诺";
                break;
            case 175:
                Country = "塞内加尔";
                break;
            case 176:
                Country = "索马里";
                break;
            case 177:
                Country = "苏里南";
                break;
            case 178:
                Country = "南苏丹";
                break;
            case 179:
                Country = "圣多美和普林西比";
                break;
            case 180:
                Country = "萨尔瓦多";
                break;
            case 181:
                Country = "叙利亚";
                break;
            case 182:
                Country = "斯威士兰";
                break;
            case 183:
                Country = "特克斯和凯科斯群岛";
                break;
            case 184:
                Country = "乍得";
                break;
            case 185:
                Country = "多哥";
                break;
            case 186:
                Country = "泰国";
                break;
            case 187:
                Country = "托克劳";
                break;
            case 188:
                Country = "东帝汶";
                break;
            case 189:
                Country = "突尼斯";
                break;
            case 190:
                Country = "汤加";
                break;
            case 191:
                Country = "土耳其";
                break;
            case 192:
                Country = "图瓦卢";
                break;
            case 193:
                Country = "坦桑尼亚";
                break;
            case 194:
                Country = "乌克兰";
                break;
            case 195:
                Country = "乌干达";
                break;
            case 196:
                Country = "美国";
                break;
            case 197:
                Country = "乌拉圭";
                break;
            case 198:
                Country = "梵蒂冈";
                break;
            case 199:
                Country = "委内瑞拉";
                break;
            case 200:
                Country = "英属维尔京群岛";
                break;
            case 201:
                Country = "美属维尔京群岛";
                break;
            case 202:
                Country = "越南";
                break;
            case 203:
                Country = "瓦利斯和富图纳";
                break;
            case 204:
                Country = "萨摩亚";
                break;
            case 205:
                Country = "也门";
                break;
            case 206:
                Country = "马约特";
                break;
            case 207:
                Country = "南非";
                break;
            case 208:
                Country = "赞比亚";
                break;
            case 209:
                Country = "津巴布韦";
                break;
            case 210:
                Country = "中国";
                break;
            case 211:
                Country = "刚果(布)";
                break;
            case 212:
                Country = "刚果(金)";
                break;
            case 213:
                Country = "莫桑比克";
                break;
            case 214:
                Country = "根西岛";
                break;
            case 215:
                Country = "冈比亚";
                break;
            case 216:
                Country = "北马里亚纳群岛";
                break;
            case 217:
                Country = "埃塞俄比亚";
                break;
            case 218:
                Country = "新喀里多尼亚";
                break;
            case 219:
                Country = "瓦努阿图";
                break;
            case 220:
                Country = "法属南部领地";
                break;
            case 221:
                Country = "纽埃";
                break;
            case 222:
                Country = "美国本土外小岛屿";
                break;
            case 223:
                Country = "库克群岛";
                break;
            case 224:
                Country = "英国";
                break;
            case 225:
                Country = "特立尼达和多巴哥";
                break;
            case 226:
                Country = "圣文森特和格林纳丁斯";
                break;
            case 227:
                Country = "中华民国（台湾）";
                break;
            case 228:
                Country = "新西兰";
                break;
            case 229:
                Country = "沙特阿拉伯";
                break;
            case 230:
                Country = "老挝";
                break;
            case 231:
                Country = "朝鲜";
                break;
            case 232:
                Country = "韩国";
                break;
            case 233:
                Country = "葡萄牙";
                break;
            case 234:
                Country = "吉尔吉斯斯坦";
                break;
            case 235:
                Country = "哈萨克斯坦";
                break;
            case 236:
                Country = "塔吉克斯坦";
                break;
            case 237:
                Country = "土库曼斯坦";
                break;
            case 238:
                Country = "乌兹别克斯坦";
                break;
            case 239:
                Country = "圣基茨和尼维斯";
                break;
            case 240:
                Country = "圣皮埃尔和密克隆";
                break;
            case 241:
                Country = "圣赫勒拿";
                break;
            case 242:
                Country = "圣卢西亚";
                break;
            case 243:
                Country = "毛里求斯";
                break;
            case 244:
                Country = "科特迪瓦";
                break;
            case 245:
                Country = "肯尼亚";
                break;
            case 246:
                Country = "蒙古国";
                break;
            default:
                Country = code;
        }

        return Country;
    }

    //名族
    public static String parseNation(int code) {
        String nation;
        switch (code) {
            case 1:
                nation = "汉";
                break;
            case 2:
                nation = "蒙古";
                break;
            case 3:
                nation = "回";
                break;
            case 4:
                nation = "藏";
                break;
            case 5:
                nation = "维吾尔";
                break;
            case 6:
                nation = "苗";
                break;
            case 7:
                nation = "彝";
                break;
            case 8:
                nation = "壮";
                break;
            case 9:
                nation = "布依";
                break;
            case 10:
                nation = "朝鲜";
                break;
            case 11:
                nation = "满";
                break;
            case 12:
                nation = "侗";
                break;
            case 13:
                nation = "瑶";
                break;
            case 14:
                nation = "白";
                break;
            case 15:
                nation = "土家";
                break;
            case 16:
                nation = "哈尼";
                break;
            case 17:
                nation = "哈萨克";
                break;
            case 18:
                nation = "傣";
                break;
            case 19:
                nation = "黎";
                break;
            case 20:
                nation = "傈僳";
                break;
            case 21:
                nation = "佤";
                break;
            case 22:
                nation = "畲";
                break;
            case 23:
                nation = "高山";
                break;
            case 24:
                nation = "拉祜";
                break;
            case 25:
                nation = "水";
                break;
            case 26:
                nation = "东乡";
                break;
            case 27:
                nation = "纳西";
                break;
            case 28:
                nation = "景颇";
                break;
            case 29:
                nation = "柯尔克孜";
                break;
            case 30:
                nation = "土";
                break;
            case 31:
                nation = "达斡尔";
                break;
            case 32:
                nation = "仫佬";
                break;
            case 33:
                nation = "羌";
                break;
            case 34:
                nation = "布朗";
                break;
            case 35:
                nation = "撒拉";
                break;
            case 36:
                nation = "毛南";
                break;
            case 37:
                nation = "仡佬";
                break;
            case 38:
                nation = "锡伯";
                break;
            case 39:
                nation = "阿昌";
                break;
            case 40:
                nation = "普米";
                break;
            case 41:
                nation = "塔吉克";
                break;
            case 42:
                nation = "怒";
                break;
            case 43:
                nation = "乌孜别克";
                break;
            case 44:
                nation = "俄罗斯";
                break;
            case 45:
                nation = "鄂温克";
                break;
            case 46:
                nation = "德昂";
                break;
            case 47:
                nation = "保安";
                break;
            case 48:
                nation = "裕固";
                break;
            case 49:
                nation = "京";
                break;
            case 50:
                nation = "塔塔尔";
                break;
            case 51:
                nation = "独龙";
                break;
            case 52:
                nation = "鄂伦春";
                break;
            case 53:
                nation = "赫哲";
                break;
            case 54:
                nation = "门巴";
                break;
            case 55:
                nation = "珞巴";
                break;
            case 56:
                nation = "基诺";
                break;
            case 57:
            case 58:
            case 59:
            case 60:
            case 61:
            case 62:
            case 63:
            case 64:
            case 65:
            case 66:
            case 67:
            case 68:
            case 69:
            case 70:
            case 71:
            case 72:
            case 73:
            case 74:
            case 75:
            case 76:
            case 77:
            case 78:
            case 79:
            case 80:
            case 81:
            case 82:
            case 83:
            case 84:
            case 85:
            case 86:
            case 87:
            case 88:
            case 89:
            case 90:
            case 91:
            case 92:
            case 93:
            case 94:
            case 95:
            case 96:
            default:
                nation = "";
                break;
            case 97:
                nation = "其他";
                break;
            case 98:
                nation = "外国血统中国籍人士";
        }

        return nation;
    }

}
