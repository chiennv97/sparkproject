/**
 * Created by chien on 5/20/2017.
 */
import com.google.common.base.MoreObjects;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import spark.Request;
import spark.Response;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static spark.Spark.get;

public class GuavaTest {
    public static void main(String[] args) {
        LoadingCache<Integer,ArrayList> primeCache = CacheBuilder.newBuilder()
                .maximumSize(100)
                .expireAfterAccess(10, TimeUnit.MINUTES).build(new CacheLoader<Integer, ArrayList>() {
                    @Override
                    public ArrayList load(Integer integer) throws Exception {
                        System.out.println("yes" + integer);
                        return listPrime(integer);
                    }
                });
        //Định tuyến và lấy giá trị của n tại đường dẫn
        get("/prime", (Request req, Response res) -> {
            String number = req.queryParams("n");
            //bắt lỗi không phải là số
            try{
                int n = Integer.parseInt(number);
                if(n<=1){
                    return "null";
                }else{
                    return primeCache.get(n);
                }
            }catch (Exception e){
                return "Not is number";
            }


        });
    }
    public static ArrayList listPrime(int n){
        ArrayList list = new ArrayList();

        for(int i =2; i<=n ;i++){
            if(isPrime(i) == true){
                list.add(i);
            }
        }
        return list;
    }
    //Trừ 2, 3 thì các số nguyên tố khác đều có dạng 6K+1 hoặc 6K-1
    //kiểm tra số n có phải số nguyên tố ko thì kiểm tra từ 2 -> căn của n
    public static boolean isPrime(int n){
        if(n ==2 || n ==3){
            return true;
        }
        //nếu có dạng 6K+2 hoặc 6k-2, 6K+3 hoặc 6K-3 thì trả về false
        if(n%2 ==0 || n%3 ==0){
            return false;
        }
        //kiểm tra trong khoảng từ [5;sqrtN]
        double sqrtN = Math.sqrt(n);
        int k =-1;
        while (k<=sqrtN){
            k+=6;
            if(n%(k)==0 || n%(k+2)==0){
                break;
            }
        }

        return k>sqrtN;
    }
}
