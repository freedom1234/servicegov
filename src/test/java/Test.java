import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by kongxfa on 2016/1/28.
 */
public class Test {
    @org.junit.Test
    public void test(){
//        String Id="%E6%B5%8B%E8%AF%95";%E5%95%8A%E5%95%8A
//        String Id="%B2%E2%CA%D4";
//        String Id="æµ\u008Bè¯";
        String Id="%E5%95%8A%E5%95%8A";
//        String Id="测试";
        try {
            Id = URLDecoder.decode(Id, "UTF-8");
//            Id = java.net.URLDecoder.decode(Id, "GBK");
            String a= java.net.URLEncoder.encode("测试","GBK");
            System.out.println(a);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("+++++++++++++++++解码后:"+Id);
    }

    @org.junit.Test
    public void test2(){
        String a="f_1";
        String[]array=a.split("_");
        for (int i = 0; i < array.length; i++) {
            System.out.println(array[i]);
            System.out.println(i);
        }
    }

    //在字符串中去数字
    @org.junit.Test
    public void test3(){
        String a="n10(MMDDhhmmss)";
        StringBuffer num=new StringBuffer();
        for(int i=0;i<a.length();i++){
            if(a.charAt(i)>=48 && a.charAt(i)<=57){
                num.append(a.charAt(i));
            }
        }
        System.out.println(num);
    }

}
