package com.environment.program.hexbindecoct;

public class HexConvert {

    private static String  convertStringToHex(String str){

        char[] chars = str.toCharArray();

        StringBuilder hex = new StringBuilder();
        for (char aChar : chars) hex.append(Integer.toHexString((int) aChar));

        return hex.toString();
    }

    static String convertHexToString(String hex){

        StringBuilder sb = new StringBuilder();
        StringBuilder sb2;
        sb2 = new StringBuilder();

        for( int i=0; i<hex.length()-1; i+=2 ){

            String s = hex.substring(i, (i + 2));
            int decimal = Integer.parseInt(s, 16);
            sb.append((char)decimal);
            sb2.append(decimal);
        }

        return sb.toString();
    }
    private static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        // toUpperCase将字符串中的所有字符转换为大写
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        // toCharArray将此字符串转换为一个新的字符数组。
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }
    /* 返回匹配字符 */
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    /* 将字节数组转换为short类型，即统计字符串长度 */
//    public static short bytes2Short2(byte[] b) {
//        return (short) (((b[1] & 0xff) << 8) | b[0] & 0xff);
//    }
    /* 将字节数组转换为16进制字符串 */
    static String BinaryToHexString(byte[] bytes) {
        String hexStr = "0123456789ABCDEF";
        StringBuilder result = new StringBuilder();
        String hex;
        for (byte b : bytes) {
            hex = String.valueOf(hexStr.charAt((b & 0xF0) >> 4));
            hex += String.valueOf(hexStr.charAt(b & 0x0F));
            result.append(hex).append(" ");
        }
        return result.toString();
    }

    public static void main(String[] args) {
        System.out.println("======ASCII码转换为16进制======");
        String str = "ABC-0001";
        System.out.println("字符串: " + str);
        String hex = HexConvert.convertStringToHex(str);
        System.out.println("====转换为16进制=====" + hex);
        System.out.println("======16进制转换为ASCII======");
        System.out.println("Hex : " + hex);
        System.out.println("ASCII : " + HexConvert.convertHexToString(hex));
        byte[] bytes = HexConvert.hexStringToBytes( hex );
        System.out.println(HexConvert.BinaryToHexString( bytes ));
    }
}