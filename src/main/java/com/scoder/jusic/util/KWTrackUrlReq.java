package com.scoder.jusic.util;


import cn.hutool.core.codec.Base64Encoder;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.URLUtil;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * @author JumpAlang
 * @create 2023-07-18 21:33
 */
public class KWTrackUrlReq {
    private final String SECRET_KEY = "ylzsxkwm";
    private final int DES_MODE_DECRYPT = 1;
    private final int[] arrayE = {
            31, 0, DES_MODE_DECRYPT, 2, 3, 4, -1, -1, 3, 4, 5, 6, 7, 8, -1, -1, 7, 8, 9, 10, 11, 12, -1, -1, 11, 12, 13, 14,
            15, 16, -1, -1, 15, 16, 17, 18, 19, 20, -1, -1, 19, 20, 21, 22, 23, 24, -1, -1, 23, 24, 25, 26, 27, 28, -1, -1,
            27, 28, 29, 30, 31, 30, -1, -1
    };
    private final int[] arrayIP = {
            57, 49, 41, 33, 25, 17, 9, DES_MODE_DECRYPT, 59, 51, 43, 35, 27, 19, 11, 3, 61, 53, 45, 37, 29, 21, 13, 5, 63,
            55, 47, 39, 31, 23, 15, 7, 56, 48, 40, 32, 24, 16, 8, 0, 58, 50, 42, 34, 26, 18, 10, 2, 60, 52, 44, 36, 28, 20,
            12, 4, 62, 54, 46, 38, 30, 22, 14, 6
    };
    private final int[] arrayIP_1 = {
            39, 7, 47, 15, 55, 23, 63, 31, 38, 6, 46, 14, 54, 22, 62, 30, 37, 5, 45, 13, 53, 21, 61, 29, 36, 4, 44, 12, 52,
            20, 60, 28, 35, 3, 43, 11, 51, 19, 59, 27, 34, 2, 42, 10, 50, 18, 58, 26, 33, DES_MODE_DECRYPT, 41, 9, 49, 17,
            57, 25, 32, 0, 40, 8, 48, 16, 56, 24
    };
    private final int[] arrayLs = {1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1};
    private final int[] arrayLsMask = {0, 0x100001, 0x300003};
    private final long[] arrayMask = new long[64];
    private final int[] arrayP = {
            15, 6, 19, 20, 28, 11, 27, 16,
            0, 14, 22, 25, 4, 17, 30, 9,
            1, 7, 23, 13, 31, 26, 2, 8,
            18, 12, 29, 5, 21, 10, 3, 24
    };
    private final int[] arrayPC_1 = {
            56, 48, 40, 32, 24, 16, 8, 0,
            57, 49, 41, 33, 25, 17, 9, 1,
            58, 50, 42, 34, 26, 18, 10, 2,
            59, 51, 43, 35, 62, 54, 46, 38,
            30, 22, 14, 6, 61, 53, 45, 37,
            29, 21, 13, 5, 60, 52, 44, 36,
            28, 20, 12, 4, 27, 19, 11, 3
    };
    private final int[] arrayPC_2 = {
            13, 16, 10, 23, 0, 4, -1, -1,
            2, 27, 14, 5, 20, 9, -1, -1,
            22, 18, 11, 3, 25, 7, -1, -1,
            15, 6, 26, 19, 12, 1, -1, -1,
            40, 51, 30, 36, 46, 54, -1, -1,
            29, 39, 50, 44, 32, 47, -1, -1,
            43, 48, 38, 55, 33, 52, -1, -1,
            45, 41, 49, 35, 28, 31, -1, -1
    };
    private final int[][] matrixNSBox = {{
            14, 4, 3, 15, 2, 13, 5, 3,
            13, 14, 6, 9, 11, 2, 0, 5,
            4, 1, 10, 12, 15, 6, 9, 10,
            1, 8, 12, 7, 8, 11, 7, 0,
            0, 15, 10, 5, 14, 4, 9, 10,
            7, 8, 12, 3, 13, 1, 3, 6,
            15, 12, 6, 11, 2, 9, 5, 0,
            4, 2, 11, 14, 1, 7, 8, 13,}, {
            15, 0, 9, 5, 6, 10, 12, 9,
            8, 7, 2, 12, 3, 13, 5, 2,
            1, 14, 7, 8, 11, 4, 0, 3,
            14, 11, 13, 6, 4, 1, 10, 15,
            3, 13, 12, 11, 15, 3, 6, 0,
            4, 10, 1, 7, 8, 4, 11, 14,
            13, 8, 0, 6, 2, 15, 9, 5,
            7, 1, 10, 12, 14, 2, 5, 9,}, {
            10, 13, 1, 11, 6, 8, 11, 5,
            9, 4, 12, 2, 15, 3, 2, 14,
            0, 6, 13, 1, 3, 15, 4, 10,
            14, 9, 7, 12, 5, 0, 8, 7,
            13, 1, 2, 4, 3, 6, 12, 11,
            0, 13, 5, 14, 6, 8, 15, 2,
            7, 10, 8, 15, 4, 9, 11, 5,
            9, 0, 14, 3, 10, 7, 1, 12,}, {
            7, 10, 1, 15, 0, 12, 11, 5,
            14, 9, 8, 3, 9, 7, 4, 8,
            13, 6, 2, 1, 6, 11, 12, 2,
            3, 0, 5, 14, 10, 13, 15, 4,
            13, 3, 4, 9, 6, 10, 1, 12,
            11, 0, 2, 5, 0, 13, 14, 2,
            8, 15, 7, 4, 15, 1, 10, 7,
            5, 6, 12, 11, 3, 8, 9, 14,}, {
            2, 4, 8, 15, 7, 10, 13, 6,
            4, 1, 3, 12, 11, 7, 14, 0,
            12, 2, 5, 9, 10, 13, 0, 3,
            1, 11, 15, 5, 6, 8, 9, 14,
            14, 11, 5, 6, 4, 1, 3, 10,
            2, 12, 15, 0, 13, 2, 8, 5,
            11, 8, 0, 15, 7, 14, 9, 4,
            12, 7, 10, 9, 1, 13, 6, 3,}, {
            12, 9, 0, 7, 9, 2, 14, 1,
            10, 15, 3, 4, 6, 12, 5, 11,
            1, 14, 13, 0, 2, 8, 7, 13,
            15, 5, 4, 10, 8, 3, 11, 6,
            10, 4, 6, 11, 7, 9, 0, 6,
            4, 2, 13, 1, 9, 15, 3, 8,
            15, 3, 1, 14, 12, 5, 11, 0,
            2, 12, 14, 7, 5, 10, 8, 13,}, {
            4, 1, 3, 10, 15, 12, 5, 0,
            2, 11, 9, 6, 8, 7, 6, 9,
            11, 4, 12, 15, 0, 3, 10, 5,
            14, 13, 7, 8, 13, 14, 1, 2,
            13, 6, 14, 9, 4, 1, 2, 14,
            11, 13, 5, 0, 1, 10, 8, 3,
            0, 11, 3, 5, 9, 4, 15, 2,
            7, 8, 12, 15, 10, 7, 6, 12,}, {
            13, 7, 10, 0, 6, 9, 5, 15,
            8, 4, 3, 10, 11, 14, 12, 5,
            2, 11, 9, 6, 15, 12, 0, 3,
            4, 1, 14, 13, 1, 2, 7, 8,
            1, 2, 12, 15, 10, 4, 0, 3,
            13, 14, 6, 9, 7, 8, 9, 6,
            15, 1, 5, 12, 3, 10, 14, 5,
            8, 7, 11, 0, 4, 13, 2, 11,}
    };

    private static String csrf = "";

    public KWTrackUrlReq() {
        for (int i = 0; i < 63; i++) arrayMask[i] = (long) Math.pow(2, i);
        arrayMask[arrayMask.length - 1] = Long.MIN_VALUE;
    }
    /**
     * 获取酷我音乐歌曲链接
     *
     * @param mid 歌曲
     * @param quality 品质(128k 320k flac)
     * @return
     */
    public String getTrackUrl3(String mid, String quality) {
        String params = "";
        switch (quality) {
            case "128k":
                params = "corp=kuwo&p2p=1&type=convert_url2&format=mp3&rid=" + mid;
                break;
            case "320k":
                params = String.format("user=0&android_id=0&prod=kwplayer_ar_9.3.1.3&corp=kuwo&newver=3&vipver=9.3.1.3&source=oppo&p2p=1&notrace=0&type=convert_url2&format=flac|mp3|aac&sig=0&rid=%s&priority=bitrate&loginUid=0&network=WIFI&loginSid=0&mode=download", mid);
                break;
            case "flac":
                params = "corp=kuwo&p2p=1&type=convert_url2&format=flac&rid=" + mid;
                break;
        }
        String url = "http://nmobi.kuwo.cn/mobi.s?f=kuwo&q=" + base64Encrypt(params);
        HttpResponse resp = Unirest.get(url)
                .header("user_agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko)" +
                        " Chrome/110.0.0.0 Safari/537.36 Edg/110.0.1587.50")
                .header("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7")
                .header("referer", "https://www.kuwo.cn/search/list?key=")
                .header("csrf", csrf)
                .asString();
        String setCookie = resp.getCookies().toString();
        if (setCookie != null && !"".equals(setCookie)) {
            String kwToken = ReUtil.get("kw_token=(.*?);", setCookie, 1);
            csrf = kwToken;
        }
        String trackUrl = ReUtil.get("url=(.*?)\r\n", resp.getBody().toString(), 1);

        return trackUrl;
    }
    public String getTrackUrl(String mid, String quality) {
        String params = "";
        switch (quality) {
            case "128k":
                params = "128kmp3";
                break;
            case "320k":
                params = "320kmp3";
                break;
            case "flac":
                params = "2000kflac";
                break;
        }
        String url = "https://mobi.kuwo.cn/mobi.s?f=web&source=kwplayer_ar_5.1.0.0_B_jiakong_vh.apk&type=convert_url_with_sign&rid="+mid+"&br="+params;
        HttpResponse resp = Unirest.get(url)
                .header("user_agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko)" +
                        " Chrome/110.0.0.0 Safari/537.36 Edg/110.0.1587.50")
                .header("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7")
                .header("referer", "https://www.kuwo.cn/search/list?key=")
                .header("csrf", csrf)
                .asString();
        String setCookie = resp.getCookies().toString();
        if (setCookie != null && !"".equals(setCookie)) {
            String kwToken = ReUtil.get("kw_token=(.*?);", setCookie, 1);
            csrf = kwToken;
        }
        String trackUrl = ReUtil.get("\"url\":\"(.*?)\"", resp.getBody().toString(), 1);
        if(trackUrl == null || trackUrl.indexOf("/4141006416.mp3") != -1 || trackUrl.indexOf("/2272659253.mp3") != -1 || trackUrl.indexOf("/2015636967.aac") != -1){
            trackUrl = getTrackUrl3(mid,quality);
        }
        return trackUrl;
    }

    public String getTrackUrl2(String mid, String quality) {
        String params = mid+"?isMv=0&format=%s&br=%s&level=";
        switch (quality) {
            case "128k":
                params = String.format(params,"mp3","128kmp3");
                break;
            case "320k":
                params = String.format(params,"mp3","320kmp3");
                break;
            case "flac":
                params = String.format(params,"flac","2000kflac");
                break;
        }
        String url = "https://bd-api.kuwo.cn/api/service/music/downloadInfo/" + params;
        HttpResponse resp = Unirest.get(url)
                .header("user_agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko)" +
                        " Chrome/110.0.0.0 Safari/537.36 Edg/110.0.1587.50")
                .header("channel", "qq")
                .header("plat","ar")
                .header("net","wifi")
                .header("ver","3.1.2")
                .header("uid","")
                .header("devId","0")
                .asString();
        String trackUrl = ReUtil.get("\"url\":\"(.*?)\"", resp.getBody().toString(), 1);
        return trackUrl;
    }


    public String searchByKeyWord(String keyWord) {
        String url = "https://search.kuwo.cn/r.s?pn=0&rn=1&all="+ URLUtil.encode(keyWord)+"&ft=music&newsearch=1&alflac=1&itemset=web_2013&client=kt&cluster=0&vermerge=1&rformat=json&encoding=utf8&show_copyright_off=1&pcmp4=1&ver=mbox&plat=pc&vipver=MUSIC_9.2.0.0_W6&devid=11404450&newver=1&issubtitle=1&pcjson=1";//"http://www.kuwo.cn/api/www/search/searchMusicBykeyWord?key="+keyWord+"&pn=1&rn=20&httpsStatus=1&reqId=a47f76b0-1c12-11ee-93a9-af6c69693772&plat=web_www&from=";
        HttpResponse resp = Unirest.get(url)
                .header("user_agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko)" +
                        " Chrome/110.0.0.0 Safari/537.36 Edg/110.0.1587.50")
                .header("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7")
                .header("referer", "http://kuwo.cn/search/list?key=%E6%81%90%E9%BE%99%E6%8A%97%E7%8B%BC8")
                .header("csrf", csrf)
                .header("Secret","13261c0dccfeac48dd7a8b33de9fd1bb59e7bcd1fbda77ed3a2e42bce5fc7e0f0036d507")
                .header("Cross","e5191b2eb629a3da9dc6868755a3e779")
                .header("Cookie","ga=GA1.2.1860922824.1635265329; Hm_lvt_cdb524f42f0ce19b169a8071123a4797=1663159268; gid=9ed7ed0b-8d4b-4167-8c9d-f1f2c55642f7; Hm_token=et7csP3xeQfeadZsDEazXEpEXhmjTC4k; Hm_Iuvt_cdb524f42f0ce19b169b8072123a4727=Mzfa6zAAcAfszyHFdREYF7KfBRNmAEi4")
                .asString();
        String mid = ReUtil.get("\"MUSICRID\":\"MUSIC_(.*?)\"", resp.getBody().toString(), 1);
        return mid;
    }

    private String base64Encrypt(String msg) {
        byte[] msgBytes = msg.getBytes(StandardCharsets.UTF_8);
        byte[] secretKeyBytes = SECRET_KEY.getBytes(StandardCharsets.UTF_8);
        byte[] b = encrypt(msgBytes, secretKeyBytes);
        String s = Base64Encoder.encode(b);
        return s.replace("\n", "");
    }

    private byte[] encrypt(byte[] msg, byte[] key) {
        // 处理密钥块
        long l = 0;
        for (int i = 0; i < 8; i++) l = l | (long) key[i] << i * 8;

        int j = msg.length / 8;
        // arrLong1 存放的是转换后的密钥块, 在解密时只需要把这个密钥块反转就行了
        long[] arrLong1 = new long[16];
        subKeys(l, arrLong1, 0);

        // arrLong2 存放的是前部分的明文
        long[] arrLong2 = new long[j];
        for (int m = 0; m < j; m++)
            for (int n = 0; n < 8; n++)
                arrLong2[m] |= (long) msg[n + m * 8] << n * 8;

        // 用于存放密文
        long[] arrLong3 = new long[(1 + 8 * (j + 1)) / 8];
        // 计算前部的数据块(除了最后一部分)
        for (int i1 = 0; i1 < j; i1++)
            arrLong3[i1] = DES64(arrLong1, arrLong2[i1]);

        // 保存多出来的字节
        byte[] arrByte1 = Arrays.copyOfRange(msg, j * 8, msg.length);
        long l2 = 0;
        for (int i1 = 0, msgLen = msg.length; i1 < msgLen % 8; i1++)
            l2 |= (long) arrByte1[i1] << i1 * 8;
        // 计算多出的那一位(最后一位)
        arrLong3[j] = DES64(arrLong1, l2);

        // 将密文转为字节型
        byte[] arrByte2 = new byte[8 * arrLong3.length];
        int i4 = 0;
        for (long l3 : arrLong3)
            for (int i6 = 0; i6 < 8; i6++)
                arrByte2[i4++] = (byte) (255 & l3 >> i6 * 8);

        return arrByte2;
    }

    private long DES64(long[] longs, long l) {
        long out;
        int SOut;
        long[] pR = new long[8];
        long[] pSource = new long[2];
        long L;
        long R;
        out = bitTransform(arrayIP, 64, l);
        pSource[0] = 0xFFFFFFFFL & out;
        pSource[1] = (-4294967296L & out) >> 32;
        for (int i = 0; i < 16; i++) {
            R = pSource[1];
            R = bitTransform(arrayE, 64, R);
            R ^= longs[i];
            for (int j = 0; j < 8; j++)
                pR[j] = 255 & R >> j * 8;
            SOut = 0;
            for (int sbi = 7; sbi > -1; sbi--) {
                SOut <<= 4;
                SOut |= matrixNSBox[sbi][(int) pR[sbi]];
            }

            R = bitTransform(arrayP, 32, SOut);
            L = pSource[0];
            pSource[0] = pSource[1];
            pSource[1] = L ^ R;
        }
        ArrayUtil.reverse(pSource);
        out = -4294967296L & pSource[1] << 32 | 0xFFFFFFFFL & pSource[0];
        out = bitTransform(arrayIP_1, 64, out);
        return out;
    }

    private void subKeys(long l, long[] longs, int n) {
        long l2 = bitTransform(arrayPC_1, 56, l);
        for (int i = 0; i < 16; i++) {
            l2 = (l2 & arrayLsMask[arrayLs[i]]) << 28 - arrayLs[i] | (l2 & ~arrayLsMask[arrayLs[i]]) >> arrayLs[i];
            longs[i] = bitTransform(arrayPC_2, 64, l2);
        }
        int j = 0;
        while (n == 1 && j < 8) {
            long t = longs[j];
            longs[j] = longs[15 - j];
            longs[15 - j] = t;
            j++;
        }
    }

    private long bitTransform(int[] arrInt, int n, long l) {
        long l2 = 0;
        for (int i = 0; i < n; i++) {
            if (arrInt[i] < 0 || (l & arrayMask[arrInt[i]]) == 0) continue;
            l2 |= arrayMask[i];
        }
        return l2;
    }

    public String getMusicUrlByKeyWord(String keyWord,String quality){
        String mid =this.searchByKeyWord(keyWord);
        if(mid == null){
            return null;
        }
        if(StringUtils.isEmpty(quality)){
            return this.getTrackUrl(mid,"320k");
        }else{
            return this.getTrackUrl(mid,quality);
        }
    }

    public static void main(String[] args) {
        KWTrackUrlReq kwTrackUrlReq = new KWTrackUrlReq();
//        String mid = kwTrackUrlReq.searchByKeyWord("吉时行乐一条龙"+"&笑喜人集团;小喜精灵;李毅杰PISSY;邓典果DDG;JarStick");
        String mid = kwTrackUrlReq.searchByKeyWord("若月亮没来(若是月亮还没来) 王宇宙Leto&乔浚丞");
        String url = kwTrackUrlReq.getTrackUrl(mid,"320k");

//        String mid2 = kwTrackUrlReq.searchByKeyWord("吉时行乐一条龙"+"+笑人集团,小精灵,李毅杰PISSY,邓典果DDG,JarStick");
//        String mid3 = kwTrackUrlReq.searchByKeyWord("学不会遗忘+庄东茹");
        //new KWTrackUrlReq().getTrackUrl("228908","320k");
//        System.out.println(mid);
//        System.out.println(kwTrackUrlReq.getTrackUrl(mid,"flac"));
//        System.out.println(kwTrackUrlReq.getTrackUrl(mid,"320k"));

        String mid2 = kwTrackUrlReq.searchByKeyWord("吉时行乐一条龙 笑人集团&小精灵&李毅杰PISSY&邓典果DDG&JarStick");
        String url2 = kwTrackUrlReq.getTrackUrl(mid2,"flac");


        String mid3 = kwTrackUrlReq.searchByKeyWord("学不会遗忘 庄东茹");
        String url3 = kwTrackUrlReq.getTrackUrl(mid3,"320k");


        String mid4 = kwTrackUrlReq.searchByKeyWord("晴天 周杰伦");
        String url4 = kwTrackUrlReq.getTrackUrl(mid4,"320k");


        String mid5 = kwTrackUrlReq.searchByKeyWord("诺言 郭有才");
        String url5 = kwTrackUrlReq.getTrackUrl(mid5,"320k");

        String mid6 = kwTrackUrlReq.searchByKeyWord(
                "别怕我伤心 七元");
        String url6 = kwTrackUrlReq.getTrackUrl(mid6,"320k");

        System.out.println("若月亮没来(若是月亮还没来) 王宇宙Leto;乔浚丞"+":"+mid);
        System.out.println(url);
        System.out.println("吉时行乐一条龙 笑人集团,小精灵,李毅杰PISSY,邓典果DDG,JarStick"+":"+mid2);
        System.out.println(url2);
        System.out.println("学不会遗忘 庄东茹"+":"+mid3);
        System.out.println(url3);
        System.out.println("周杰伦 晴天"+":"+mid4);
        System.out.println(url4);
        System.out.println("诺言 郭有才"+":"+mid5);
        System.out.println(url5);

        System.out.println("别怕我伤心 七元"+":"+mid6);
        System.out.println(url6);
        //        System.out.println(kwTrackUrlReq.getTrackUrl(mid3,"flac"));

//        String url = kwTrackUrlReq.getTrackUrl2("94239","320k");
//        System.out.println(url);
//        HttpResponse response = Unirest.get("https://api.live.bilibili.com/room/v1/Danmu/getConf?room_id=1026846&platform=pc&player=web").asString();
//        System.out.println(response);
    }

}