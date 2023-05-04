package tw.edu.pu.s1091802.googlemap;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import tw.edu.pu.s1091802.googlemap.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback , LocationListener , GoogleMap.OnMarkerClickListener , GoogleApiClient.ConnectionCallbacks , GoogleApiClient.OnConnectionFailedListener
{

    public static final int ROUND = 10;
    private GoogleMap mMap;
    public GoogleApiClient googleApiClient;
    Marker marker;
    public FusedLocationProviderClient fusedLocationProviderClient;
    private LocationManager locMgr;
    float zoom;
    String bestProv;

    @Override
    public void onLocationChanged(Location location)
    {
        //取得地圖座標值 : 緯度 , 經度
        String x = "緯=" + Double.toString(location.getLatitude());
        String y = "經=" + Double.toString(location.getLongitude());

        LatLng Point = new LatLng(location.getLatitude(), location.getLongitude());
        zoom = 17;
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(Point, zoom));

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            mMap.setMyLocationEnabled(true);  //顯示定位圖示
        }

        Toast.makeText(this, x + "\n" + y, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        ActivityMapsBinding binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Button nextPageBtn = (Button)findViewById(R.id.button);  //跳轉頁面
        nextPageBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
            Intent intent = new Intent();
            intent.setClass(MapsActivity.this, search.class);  //從主畫面跳到搜尋畫面
            startActivity(intent);
            }
        }
        );
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        locMgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        bestProv = locMgr.getBestProvider(criteria, true);  //取得最佳定位方式

        //如果GPS或網路定位開啟，更新位置
        if (locMgr.isProviderEnabled(LocationManager.GPS_PROVIDER) || locMgr.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
        {
            //確認 ACCESS_FINE_LOCATION 權限是否授權
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            {
                locMgr.requestLocationUpdates(bestProv, 1000, 1, this);
            }
        }
        else
        {
            Toast.makeText(this, "請開啟定位服務", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        //確認 ACCESS_FINE_LOCATION 權限是否授權
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            locMgr.removeUpdates(this);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras)
    {
        Criteria criteria = new Criteria();
        bestProv = locMgr.getBestProvider(criteria, true);
    }

    @Override
    public void onProviderEnabled(String provider)
    {

    }

    @Override
    public void onProviderDisabled(String provider)
    {

    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;
        googleMap.setOnMarkerClickListener(this);

        LatLng 大甲體育場 = new LatLng(24.3538904, 120.6126438);
        mMap.addMarker(new MarkerOptions().position(大甲體育場).title("大甲體育場").snippet("臺中市大甲區大安港路2號"));
        LatLng 大甲區網球場 = new LatLng(24.341066, 120.62919999999997);
        mMap.addMarker(new MarkerOptions().position(大甲區網球場).title("大甲區網球場").snippet("臺中市大甲區東陽新村71-15號"));
        LatLng 大肚運一運動公園 = new LatLng(24.341066, 120.62919999999997);
        mMap.addMarker(new MarkerOptions().position(大肚運一運動公園).title("大肚運一運動公園").snippet("臺中市大肚區沙田路三段247號"));
        LatLng 大里游泳池 = new LatLng(24.1089129, 120.6874219);
        mMap.addMarker(new MarkerOptions().position(大里游泳池).title("大里游泳池").snippet("臺中市大里區東榮路55巷3號"));
        LatLng 公五網球場 = new LatLng(24.1159089, 120.67965800000002);
        mMap.addMarker(new MarkerOptions().position(公五網球場).title("公五網球場").snippet("臺中市大里區國光路大買家2點鐘方向"));
        LatLng 大里運動公園 = new LatLng(24.0983586, 120.68334870000001);
        mMap.addMarker(new MarkerOptions().position(大里運動公園).title("大里運動公園").snippet("臺中市大里區國光路與大里路口"));
        LatLng 大雅區六寶槌球場 = new LatLng(24.2540339, 120.6001738);
        mMap.addMarker(new MarkerOptions().position(大雅區六寶槌球場).title("大雅區六寶槌球場").snippet("清泉崗空軍基地大門口斜對面"));
        LatLng 太平區體育場 = new LatLng(24.130993, 120.721122);
        mMap.addMarker(new MarkerOptions().position(太平區體育場).title("太平區體育場").snippet("臺中市太平區中興東路99號"));
        LatLng 太平游泳池 = new LatLng(24.1200121, 120.71822139999995);
        mMap.addMarker(new MarkerOptions().position(太平游泳池).title("太平游泳池").snippet("臺中市太平區永成北路306號"));
        LatLng 祥順運動公園 = new LatLng(24.1375515, 120.7125969);
        mMap.addMarker(new MarkerOptions().position(祥順運動公園).title("祥順運動公園").snippet("臺中市太平區宜昌路87巷(74號快速道路旁)"));
        LatLng 中興籃槌球場 = new LatLng(24.1769394, 120.67688640000006);
        mMap.addMarker(new MarkerOptions().position(中興籃槌球場).title("中興籃槌球場").snippet("臺中市北屯區大連路、山西路口"));
        LatLng 中興網球場 = new LatLng(24.281667, 120.675361);
        mMap.addMarker(new MarkerOptions().position(中興網球場).title("中興網球場").snippet("臺中市北屯區山西路二段231號"));
        LatLng 中興游泳池 = new LatLng(24.175349, 120.676288);
        mMap.addMarker(new MarkerOptions().position(中興游泳池).title("中興游泳池").snippet("臺中市北屯區山西路二段233號"));
        LatLng 太原棒球場 = new LatLng(24.1610308, 120.72048340000003);
        mMap.addMarker(new MarkerOptions().position(太原棒球場).title("太原棒球場").snippet("臺中市北屯區建和路一段和建軍一街交叉"));
        LatLng 極限運動場 = new LatLng(24.1834951, 120.68732890000001);
        mMap.addMarker(new MarkerOptions().position(極限運動場).title("極限運動場").snippet("臺中市北屯區崇德八路1段93號"));
        LatLng 洲際棒球場 = new LatLng(24.199694, 120.684013);
        mMap.addMarker(new MarkerOptions().position(洲際棒球場).title("洲際棒球場").snippet("臺中市北屯區崇德路三段835號"));
        LatLng 兒童公園網球場 = new LatLng(24.167944, 120.694502);
        mMap.addMarker(new MarkerOptions().position(兒童公園網球場).title("兒童公園網球場").snippet("台中市北屯區文昌東十一街"));
        LatLng 土牛運動公園 = new LatLng(24.266841, 120.79647999999997);
        mMap.addMarker(new MarkerOptions().position(土牛運動公園).title("土牛運動公園").snippet("臺中市石岡區豐勢路轉國孝巷直行700M"));
        LatLng 臺中市立慢速壘球場 = new LatLng(24.2879619, 120.7289015);
        mMap.addMarker(new MarkerOptions().position(臺中市立慢速壘球場).title("臺中市立慢速壘球場").snippet("臺中市后里區星科路底"));
        LatLng 三信游泳池 = new LatLng(24.1669745, 120.66287920000002);
        mMap.addMarker(new MarkerOptions().position(三信游泳池).title("三信游泳池").snippet("臺中市西屯區寧夏東七街2號"));
        LatLng 健康公園溜冰場 = new LatLng(24.119528, 120.668083);
        mMap.addMarker(new MarkerOptions().position(健康公園溜冰場).title("健康公園溜冰場").snippet("臺中市南區五權南路與忠明南路口"));
        LatLng 萬壽棒球場 = new LatLng(24.146603, 120.650608);
        mMap.addMarker(new MarkerOptions().position(萬壽棒球場).title("萬壽棒球場").snippet("臺中市西區向上路與大墩路交叉口"));
        LatLng 沙鹿運十八運動場 = new LatLng(24.2169352, 120.571552);
        mMap.addMarker(new MarkerOptions().position(沙鹿運十八運動場).title("沙鹿運十八運動場").snippet("臺中市沙鹿區南陽路376號"));
        LatLng 沙鹿運十九慢壘場 = new LatLng(24.2090988, 120.5639013);
        mMap.addMarker(new MarkerOptions().position(沙鹿運十九慢壘場).title("沙鹿運十九慢壘場").snippet("臺中市沙鹿區保順一街134號1.2樓"));
        LatLng 沙鹿游泳池 = new LatLng(24.233504, 120.564402);
        mMap.addMarker(new MarkerOptions().position(沙鹿游泳池).title("沙鹿游泳池").snippet("臺中市沙鹿區鎮南路二段486號"));
        LatLng 東峰游泳池 = new LatLng(24.1234034, 120.68508810000003);
        mMap.addMarker(new MarkerOptions().position(東峰游泳池).title("東峰游泳池").snippet("臺中市東區七中路37號"));
        LatLng 東勢網球場 = new LatLng(24.2602429, 120.82420200000001);
        mMap.addMarker(new MarkerOptions().position(東勢網球場).title("東勢網球場").snippet("臺中市東勢區公園路136號"));
        LatLng 東勢游泳池 = new LatLng(24.2602429, 120.82420200000001);
        mMap.addMarker(new MarkerOptions().position(東勢游泳池).title("東勢游泳池").snippet("臺中市東勢區公園路136號"));
        LatLng 烏日區籃球場 = new LatLng(24.1072684,120.62582500000008);
        mMap.addMarker(new MarkerOptions().position(烏日區籃球場).title("烏日區籃球場").snippet("臺中市烏日區光日路228號旁"));
        LatLng 烏日區網球場 = new LatLng(24.1075951, 120.62522799999999);
        mMap.addMarker(new MarkerOptions().position(烏日區網球場).title("烏日區網球場").snippet("臺中市烏日區光日路228號旁"));
        LatLng 烏日溫水游泳池 = new LatLng(24.0616078, 120.63872859999992);
        mMap.addMarker(new MarkerOptions().position(烏日溫水游泳池).title("烏日溫水游泳池").snippet("臺中市烏日區慶光路798號"));
        LatLng 烏日區游泳池 = new LatLng(24.1033055, 120.61686770000006);
        mMap.addMarker(new MarkerOptions().position(烏日區游泳池).title("烏日區游泳池").snippet("臺中市烏日區環河路三段736巷10號"));
        LatLng 梧棲運七簡易棒壘球場 = new LatLng(24.258556, 120.543972);
        mMap.addMarker(new MarkerOptions().position(梧棲運七簡易棒壘球場).title("梧棲運七簡易棒壘球場").snippet("臺中市梧棲區大仁路一段465巷"));
        LatLng 梧棲運八射箭場 = new LatLng(24.254722, 120.548972);
        mMap.addMarker(new MarkerOptions().position(梧棲運八射箭場).title("梧棲運八射箭場").snippet("臺中市梧棲區大智路一段646號"));
        LatLng 梧棲運十二田徑場 = new LatLng(24.250778, 120.543556);
        mMap.addMarker(new MarkerOptions().position(梧棲運十二田徑場).title("梧棲運十二田徑場").snippet("臺中市梧棲區中正路303巷301號"));
        LatLng 梧棲運十五運動公園 = new LatLng(24.247278, 120.529194);
        mMap.addMarker(new MarkerOptions().position(梧棲運十五運動公園).title("梧棲運十五運動公園").snippet("臺中市梧棲區文昌路與文化路"));
        LatLng 梧棲運九棒球場 = new LatLng(24.255139, 120.555639);
        mMap.addMarker(new MarkerOptions().position(梧棲運九棒球場).title("梧棲運九棒球場").snippet("臺中市梧棲區博愛北路259號附近"));
        LatLng 梧棲運十三網球場 = new LatLng(24.246583, 120.551556);
        mMap.addMarker(new MarkerOptions().position(梧棲運十三網球場).title("梧棲運十三網球場").snippet("臺中市梧棲區博愛路215號"));
        LatLng 梧棲運十三曲棍球場 = new LatLng(24.246611, 120.551444);
        mMap.addMarker(new MarkerOptions().position(梧棲運十三曲棍球場).title("梧棲運十三曲棍球場").snippet("臺中市梧棲區博愛路215號"));
        LatLng 清水運一運動公園 = new LatLng(24.2787125, 120.55731309999999);
        mMap.addMarker(new MarkerOptions().position(清水運一運動公園).title("清水運一運動公園").snippet("臺中市清水區五權二街與五權路(大秀國小旁)"));
        LatLng 清水運五簡易壘球場 = new LatLng(24.2629562, 120.56414659999996);
        mMap.addMarker(new MarkerOptions().position(清水運五簡易壘球場).title("清水運五簡易壘球場").snippet("臺中市清水區南社路"));
        LatLng 梧棲區游泳池 = new LatLng(24.255639, 120.533556);
        mMap.addMarker(new MarkerOptions().position(梧棲區游泳池).title("梧棲區游泳池").snippet("臺中市梧棲區中興路219號"));
        LatLng 清水運三槺榔運動公園 = new LatLng(24.2637276, 120.54595100000006);
        mMap.addMarker(new MarkerOptions().position(清水運三槺榔運動公園).title("清水運三槺榔運動公園").snippet("臺中市清水區槺榔一街9號"));
        LatLng 清水運四牛埔仔運動公園 = new LatLng(24.2657825, 120.55462869999996);
        mMap.addMarker(new MarkerOptions().position(清水運四牛埔仔運動公園).title("清水運四牛埔仔運動公園").snippet("臺中市清水區鰲峰路、民族路口"));
        LatLng 臺中市立自由車場 = new LatLng(24.272458, 120.582665);
        mMap.addMarker(new MarkerOptions().position(臺中市立自由車場).title("臺中市立自由車場").snippet("臺中市清水區鰲海路70號"));
        LatLng 新社游泳池 = new LatLng(24.238473, 120.80673790000003);
        mMap.addMarker(new MarkerOptions().position(新社游泳池).title("新社游泳池").snippet("臺中市新社區興安路1之1號"));
        LatLng 潭子沙攤排球場 = new LatLng(24.209111, 120.703667);
        mMap.addMarker(new MarkerOptions().position(潭子沙攤排球場).title("潭子沙攤排球場").snippet("臺中市潭子區中山路二段237巷2號"));
        LatLng 潭子區勝利運動公園 = new LatLng(24.209556, 120.701306);
        mMap.addMarker(new MarkerOptions().position(潭子區勝利運動公園).title("潭子區勝利運動公園").snippet("臺中市潭子區甘蔗里勝利路100號"));
        LatLng 龍井三德游泳池 = new LatLng(24.203083, 120.524833);
        mMap.addMarker(new MarkerOptions().position(龍井三德游泳池).title("龍井三德游泳池").snippet("臺中市龍井區三德里中央路2段2巷45號"));
        LatLng 臺中市立豐原體育場 = new LatLng(24.260823, 120.717475);
        mMap.addMarker(new MarkerOptions().position(臺中市立豐原體育場).title("臺中市立豐原體育場").snippet("臺中市豐原區豐北街221號"));
        LatLng 坑口運動公園 = new LatLng(24.0146808, 120.69598310000003);
        mMap.addMarker(new MarkerOptions().position(坑口運動公園).title("坑口運動公園").snippet("臺中市霧峰區坑口里中正路46號(921地震教育園區旁)"));
        LatLng 霧峰區健體中心 = new LatLng(24.0596932, 120.6999108);
        mMap.addMarker(new MarkerOptions().position(霧峰區健體中心).title("霧峰區健體中心").snippet("臺中市霧峰區成功路200號"));
        LatLng 金龍棒球場 = new LatLng(24.138806, 120.709500);
        mMap.addMarker(new MarkerOptions().position(金龍棒球場).title("金龍棒球場").snippet("台中市東區東英六街及德興一街"));
        LatLng 公六慢速壘球場 = new LatLng(24.1127726, 120.67354640000008);
        mMap.addMarker(new MarkerOptions().position(公六慢速壘球場).title("公六慢速壘球場").snippet("臺中市大里區永隆六街"));
        LatLng 豐原國民運動中心 = new LatLng(24.2431451, 120.70330979999994);
        mMap.addMarker(new MarkerOptions().position(豐原國民運動中心).title("豐原國民運動中心").snippet("臺中市豐原區豐原大道一段"));
        LatLng 北屯國民運動中心 = new LatLng(24.183289, 120.685694);
        mMap.addMarker(new MarkerOptions().position(北屯國民運動中心).title("北屯國民運動中心").snippet("臺中市北屯區文教7用地(北屯區公所對面)"));
        LatLng 朝馬國民運動中心 = new LatLng(24.168205, 120.633409);
        mMap.addMarker(new MarkerOptions().position(朝馬國民運動中心).title("朝馬國民運動中心").snippet("臺中市西屯區朝貴路199號"));
        LatLng 南屯國民運動中心 = new LatLng(24.1378484, 120.6364947);
        mMap.addMarker(new MarkerOptions().position(南屯國民運動中心).title("南屯國民運動中心").snippet("臺中市南屯區黎明路一段998號"));
        LatLng 太平國民運動中心 = new LatLng(24.148549, 120.73030960000006);
        mMap.addMarker(new MarkerOptions().position(太平國民運動中心).title("太平國民運動中心").snippet("臺中市太平區坪林森林公園"));
        LatLng 北區國民運動中心 = new LatLng(24.157101, 120.684126);
        mMap.addMarker(new MarkerOptions().position(北區國民運動中心).title("北區國民運動中心").snippet("臺中市北區崇德路一段55號"));
        LatLng 大里國民暨兒童運動中心 = new LatLng(24.1006035, 120.68312400000002);
        mMap.addMarker(new MarkerOptions().position(大里國民暨兒童運動中心).title("大里國民暨兒童運動中心").snippet("臺中市大里區國光路與大里路交叉口"));
        LatLng 潭子國民暨兒童運動中心 = new LatLng(24.210362, 120.701888);
        mMap.addMarker(new MarkerOptions().position(潭子國民暨兒童運動中心).title("潭子國民暨兒童運動中心").snippet("臺中市潭子區興華段"));
        LatLng 長春國民暨兒童運動中心 = new LatLng(24.130812, 120.680224);
        mMap.addMarker(new MarkerOptions().position(長春國民暨兒童運動中心).title("長春國民暨兒童運動中心").snippet("台中市南區合作街46號"));
        LatLng 沙鹿網球場 = new LatLng(24.2333641, 120.5662175);
        mMap.addMarker(new MarkerOptions().position(沙鹿網球場).title("沙鹿網球場").snippet("台中市沙鹿區鎮政路8號"));
        LatLng 霧峰綜合運動場 = new LatLng(24.0750527, 120.71259680000003);
        mMap.addMarker(new MarkerOptions().position(霧峰綜合運動場).title("霧峰綜合運動場").snippet("霧峰區吉峰里民生路"));
        LatLng 吉峰搥球場 = new LatLng(24.0728002, 120.71487669999999);
        mMap.addMarker(new MarkerOptions().position(吉峰搥球場).title("吉峰搥球場").snippet("臺中市霧峰區吉峰里峰堤路69號"));
        LatLng 吉峰里簡易籃球場 = new LatLng(24.079805, 120.712401);
        mMap.addMarker(new MarkerOptions().position(吉峰里簡易籃球場).title("吉峰里簡易籃球場").snippet("臺中市霧峰區大愛路198號"));
        LatLng 本堂里簡易籃球場 = new LatLng(24.057777459724885, 120.69804810000005);
        mMap.addMarker(new MarkerOptions().position(本堂里簡易籃球場).title("本堂里簡易籃球場").snippet("臺中市霧峰區中正路-臺電後方"));
        LatLng 丁台里簡易籃球場 = new LatLng(24.0471094, 120.67443700000001);
        mMap.addMarker(new MarkerOptions().position(丁台里簡易籃球場).title("丁台里簡易籃球場").snippet("臺中市霧峰區丁台里丁台路444"));
        LatLng 舊正里簡易籃球場 = new LatLng(24.0190731, 120.69188029999998);
        mMap.addMarker(new MarkerOptions().position(舊正里簡易籃球場).title("舊正里簡易籃球場").snippet("臺中市霧峰區舊正里光明路116巷54號"));
        LatLng 臺中市網球中心 = new LatLng(24.164678, 120.729830);
        mMap.addMarker(new MarkerOptions().position(臺中市網球中心).title("臺中市網球中心").snippet("臺中市北屯區祥順二路224號"));
        LatLng 甲寅里厝後廣場籃球場 = new LatLng(24.0683421, 120.70079450000003);
        mMap.addMarker(new MarkerOptions().position(甲寅里厝後廣場籃球場).title("甲寅里厝後廣場籃球場").snippet("臺中市霧峰區甲寅里德泰街68號"));
        LatLng 甲寅里屠宰場籃球場 = new LatLng(24.0693968, 120.69808439999997);
        mMap.addMarker(new MarkerOptions().position(甲寅里屠宰場籃球場).title("甲寅里屠宰場籃球場").snippet("臺中市霧峰區甲寅里中正路1105巷15號"));
        LatLng 四德里簡易籃球場 = new LatLng(24.0615254, 120.6696306);
        mMap.addMarker(new MarkerOptions().position(四德里簡易籃球場).title("四德里簡易籃球場").snippet("臺中市霧峰區四德里四德路"));
        LatLng 坑口里簡易球場 = new LatLng(24.1120554, 120.65136050000001);
        mMap.addMarker(new MarkerOptions().position(坑口里簡易球場).title("坑口里簡易球場(籃、網球)").snippet("臺中市霧峰區復興路1段42號"));
        LatLng 外埔籃球場 = new LatLng(24.3374715, 120.64579939999999);
        mMap.addMarker(new MarkerOptions().position(外埔籃球場).title("外埔籃球場").snippet("臺中市外埔區甲后路4段（外埔國小操場旁）"));
        LatLng 清水國民運動中心 = new LatLng(24.271902, 120.549810);
        mMap.addMarker(new MarkerOptions().position(清水國民運動中心).title("清水國民運動中心").snippet("臺中市清水區體2用地"));
        LatLng 足球運動休閒園區 = new LatLng(24.134196, 120.629180);
        mMap.addMarker(new MarkerOptions().position(足球運動休閒園區).title("足球運動休閒園區").snippet("臺中市南屯區龍富九路跟益豐路三段 南屯文中小用地"));
        LatLng 大甲運動公園 = new LatLng(24.339703, 120.629948);
        mMap.addMarker(new MarkerOptions().position(大甲運動公園).title("大甲運動公園").snippet("臺中市大甲區體2用地"));
        LatLng 港區運動公園 = new LatLng(24.204125, 120.564220);
        mMap.addMarker(new MarkerOptions().position(港區運動公園).title("港區運動公園").snippet("臺中市沙鹿區三鹿里10鄰自強路301號"));
        LatLng 文高8 = new LatLng(24.249111, 120.529861);
        mMap.addMarker(new MarkerOptions().position(文高8).title("文高8(棒壘球、籃球、網球場)").snippet("臺中市梧棲區文昌路與文化路交叉口"));
        LatLng 南埔簡易棒球場 = new LatLng(24.340361, 120.560639);
        mMap.addMarker(new MarkerOptions().position(南埔簡易棒球場).title("南埔簡易棒球場").snippet("臺中市大安區南埔里南安路480號(大安南埔清潔隊旁)"));
        LatLng 烏溪簡易籃球場 = new LatLng(24.190472, 120.515333);
        mMap.addMarker(new MarkerOptions().position(烏溪簡易籃球場).title("烏溪簡易籃球場").snippet("臺中市龍井區福田村護岸路西側河堤"));
        LatLng 河濱壘球場 = new LatLng(24.189694, 120.515556);
        mMap.addMarker(new MarkerOptions().position(河濱壘球場).title("河濱壘球場").snippet("臺中市龍井區護岸路與中央南路西側堤岸"));
        LatLng 龍井分駐所旁籃球場 = new LatLng(24.191833, 120.545361);
        mMap.addMarker(new MarkerOptions().position(龍井分駐所旁籃球場).title("龍井分駐所旁籃球場").snippet("臺中市龍井區沙田路四段245號後方"));
        LatLng 竹坑活動中心旁籃球場 = new LatLng(24.179806, 120.546889);
        mMap.addMarker(new MarkerOptions().position(竹坑活動中心旁籃球場).title("竹坑活動中心旁籃球場").snippet("臺中市龍井區竹坑里竹師路一段164巷2號旁(竹坑活動中心)"));
        LatLng 麗水籃球場 = new LatLng(24.200194, 120.498333);
        mMap.addMarker(new MarkerOptions().position(麗水籃球場).title("麗水籃球場").snippet("台中市龍井區麗水里5鄰三港路水裡港巷26號(龍井福順宮旁)"));
        LatLng 福田西濱橋下籃球場 = new LatLng(24.196639, 120.511583);
        mMap.addMarker(new MarkerOptions().position(福田西濱橋下籃球場).title("福田西濱橋下籃球場").snippet("臺中市龍井區西濱橋下(福田宮旁)"));
        LatLng 南寮里中沙路南寮巷籃球場 = new LatLng(24.174583, 120.567722);
        mMap.addMarker(new MarkerOptions().position(南寮里中沙路南寮巷籃球場).title("南寮里中沙路南寮巷籃球場").snippet("臺中市龍井區南寮里中沙路南寮巷22號旁"));
        LatLng 新庄里向上路六段槌球場 = new LatLng(24.183750, 120.574583);
        mMap.addMarker(new MarkerOptions().position(新庄里向上路六段槌球場).title("新庄里向上路六段槌球場").snippet("臺中市龍井區向上路六段(兒童公園旁)"));
        LatLng 中彰橋下槌球場 = new LatLng(24.194028, 120.511278);
        mMap.addMarker(new MarkerOptions().position(中彰橋下槌球場).title("中彰橋下槌球場").snippet("臺中市龍井區沙田路四段247號(中彰橋下)"));
        LatLng 龍井龍田陸橋曲棍球場及溜冰場 = new LatLng(24.191028, 120.542667);
        mMap.addMarker(new MarkerOptions().position(龍井龍田陸橋曲棍球場及溜冰場).title("龍井龍田陸橋曲棍球場及溜冰場").snippet("臺中市龍井區龍田陸橋下"));
        LatLng 中興橋下綜合球場籃球場滑輪直排輪場 = new LatLng(24.218028, 120.543944);
        mMap.addMarker(new MarkerOptions().position(中興橋下綜合球場籃球場滑輪直排輪場).title("中興橋下綜合球場籃球場滑輪直排輪場").snippet("臺中市龍井區中興橋下"));
        LatLng 山腳里龍山國小西面籃球場 = new LatLng(24.214167, 120.549306);
        mMap.addMarker(new MarkerOptions().position(山腳里龍山國小西面籃球場).title("山腳里龍山國小西面籃球場").snippet("臺中市龍井區竹師路二段112巷46號旁(龍山國小西面)"));
        LatLng 國際壘球運動園區 = new LatLng(24.3463290, 120.6435762);
        mMap.addMarker(new MarkerOptions().position(國際壘球運動園區).title("國際壘球運動園區").snippet("臺中市外埔區長生路(大甲高工斜對面約300公尺處)"));
        LatLng 霧峰舊正棒球園區 = new LatLng(24.015288, 120.695923);
        mMap.addMarker(new MarkerOptions().position(霧峰舊正棒球園區).title("霧峰舊正棒球園區").snippet("臺中市霧峰區中正路、光明路交叉口"));
        LatLng 二信游泳池 = new LatLng(24.131147, 120.659387);
        mMap.addMarker(new MarkerOptions().position(二信游泳池).title("二信游泳池").snippet("臺中市南區柳川西路一段45號"));
        LatLng 水源館 = new LatLng(24.148806, 120.689988);
        mMap.addMarker(new MarkerOptions().position(水源館).title("水源館").snippet("臺中市北區水源路25-1號"));
        LatLng 文小91足球場 = new LatLng(24.17411954930602, 120.62494399814615);
        mMap.addMarker(new MarkerOptions().position(文小91足球場).title("文小91足球場").snippet("臺中市西屯區虹揚橋邊"));
        LatLng 朝馬足球場 = new LatLng(24.169750, 120.633083);
        mMap.addMarker(new MarkerOptions().position(朝馬足球場).title("朝馬足球場").snippet("臺中市西屯區朝馬路145號"));
        LatLng 巨蛋體育館 = new LatLng(24.200190, 120.666510);
        mMap.addMarker(new MarkerOptions().position(巨蛋體育館).title("巨蛋體育館").snippet("臺中市北屯區文中小6用地"));
        LatLng 大里區大突寮溜冰場 = new LatLng(24.0995448, 120.6951862);
        mMap.addMarker(new MarkerOptions().position(大里區大突寮溜冰場).title("大里區大突寮溜冰場").snippet("臺中市大里十九甲消防隊前面河堤"));
        LatLng 霧峰區萬豐里簡易籃球場 = new LatLng(24.023218, 120.6979037);
        mMap.addMarker(new MarkerOptions().position(霧峰區萬豐里簡易籃球場).title("霧峰區萬豐里簡易籃球場").snippet("臺中市霧峰區萬豐里中正路146巷59弄右轉到底"));
        LatLng 東湖壘球場 = new LatLng(24.0869578, 120.6934591);
        mMap.addMarker(new MarkerOptions().position(東湖壘球場).title("東湖壘球場").snippet("臺中市大里區中湖路100號"));
        LatLng 長榮里溜冰場 = new LatLng(24.1089129, 120.6874219);
        mMap.addMarker(new MarkerOptions().position(長榮里溜冰場).title("長榮里溜冰場").snippet("臺中市大里區德芳路一段160號對面(大里游泳池旁)"));
        LatLng 健民槌球場 = new LatLng(24.088364, 120.7306661);
        mMap.addMarker(new MarkerOptions().position(健民槌球場).title("健民槌球場").snippet("臺中市大里區健民里健東路上竹子坑集貨場附近"));
        LatLng 高灘地的棒球場 = new LatLng(24.084087, 120.7087236);
        mMap.addMarker(new MarkerOptions().position(高灘地的棒球場).title("高灘地的棒球場").snippet("臺中市大里區美群路155巷與塗城路121巷之間(美群國小旁)"));
        LatLng 大里區桌球場 = new LatLng(24.1081743, 120.6859986);
        mMap.addMarker(new MarkerOptions().position(大里區桌球場).title("大里區桌球場").snippet("臺中市大里區德芳路一段229號3樓"));
        LatLng 瑞城公園溜冰場 = new LatLng(24.0823144, 120.7043826);
        mMap.addMarker(new MarkerOptions().position(瑞城公園溜冰場).title("瑞城公園溜冰場").snippet("臺中市大里區中興路一段2巷159弄16號"));
        LatLng 沙鹿簡易運動場 = new LatLng(24.2341229, 120.565073);
        mMap.addMarker(new MarkerOptions().position(沙鹿簡易運動場).title("沙鹿簡易運動場(籃球場)").snippet("臺中市沙鹿區鎮政路與中正街交叉口(沙鹿公園旁)"));
        LatLng 沙鹿運十一 = new LatLng(24.2347107, 120.5668801);
        mMap.addMarker(new MarkerOptions().position(沙鹿運十一).title("沙鹿運十一").snippet("臺中市沙鹿區中山路625號旁巷道右轉錦華街(沙鹿稽徵所後方)"));
        LatLng 沙鹿運十四 = new LatLng(24.2490922, 120.5550213);
        mMap.addMarker(new MarkerOptions().position(沙鹿運十四).title("沙鹿運十四").snippet("臺中市沙鹿區光明街260號對面(中華路與中棲路口)"));
        LatLng 沙鹿運六 = new LatLng(24.255608, 120.570016);
        mMap.addMarker(new MarkerOptions().position(沙鹿運六).title("沙鹿運六").snippet("臺中市沙鹿區星河路209號(鹿峰國小裡)"));
        LatLng 大肚游泳池 = new LatLng(24.162799, 120.543284);
        mMap.addMarker(new MarkerOptions().position(大肚游泳池).title("大肚游泳池").snippet("臺中市大肚區頂街里華山路152號"));
        LatLng 幸福里籃球場 = new LatLng(24.396441, 120.660536);
        mMap.addMarker(new MarkerOptions().position(幸福里籃球場).title("幸福里籃球場").snippet("臺中市大甲區黎明路與中山路交叉口(橋興生鮮超市後方)"));
        LatLng 太平槌球場 = new LatLng(24.1249665, 120.7333769);
        mMap.addMarker(new MarkerOptions().position(太平槌球場).title("太平槌球場").snippet("臺中市太平區光興路1463巷33弄67號對面"));
        LatLng 太平河堤慢壘球場 = new LatLng(24.1178228, 120.7249846);
        mMap.addMarker(new MarkerOptions().position(太平河堤慢壘球場).title("太平河堤慢壘球場").snippet("臺中市太平區太堤西路跟太平路交接(河堤公園旁)"));
        LatLng 太平區網球場 = new LatLng(24.1178228, 120.7249846);
        mMap.addMarker(new MarkerOptions().position(太平區網球場).title("太平區網球場").snippet("臺中市太平區太堤西路跟太平路交接(河堤公園旁)"));
        LatLng 臺中公園網球場北二面南二面 = new LatLng(24.1448185, 120.6844762);
        mMap.addMarker(new MarkerOptions().position(臺中公園網球場北二面南二面).title("臺中公園網球場北二面南二面").snippet("台中市北區雙十路一段65號"));
        LatLng 太原足球場 = new LatLng(24.1612361, 120.7200986);
        mMap.addMarker(new MarkerOptions().position(太原足球場).title("太原足球場").snippet("臺中市北屯區建軍一街6-8號"));
        LatLng 文中10棒球場 = new LatLng(24.1996448, 120.6840252);
        mMap.addMarker(new MarkerOptions().position(文中10棒球場).title("文中10棒球場").snippet("洲際棒球場(臺中市北屯區崇德路三段835號)對面 "));
        LatLng 大雅游泳池 = new LatLng(24.2358911, 120.6529319);
        mMap.addMarker(new MarkerOptions().position(大雅游泳池).title("大雅游泳池").snippet("臺中市大雅區神林路一段171巷80號"));
        LatLng 永和路公有網球場 = new LatLng(24.2126871, 120.6311099);
        mMap.addMarker(new MarkerOptions().position(永和路公有網球場).title("永和路公有網球場").snippet("大雅區永和路第四公墓納骨塔旁"));
        LatLng 第七公墓簡易棒球場預定地 = new LatLng(24.2298702, 120.6713114);
        mMap.addMarker(new MarkerOptions().position(第七公墓簡易棒球場預定地).title("第七公墓簡易棒球場預定地").snippet("台中市大雅區三和段67地號"));
        LatLng 大雅體育園區棒球場 = new LatLng(24.2404326, 120.6741789);
        mMap.addMarker(new MarkerOptions().position(大雅體育園區棒球場).title("大雅體育園區棒球場").snippet("臺中市大甲區東陽新村71-15號"));
        LatLng 石岡游泳池 = new LatLng(24.341066, 120.62919999999997);
        mMap.addMarker(new MarkerOptions().position(石岡游泳池).title("石岡游泳池").snippet("臺中市大甲區東陽新村71-15號"));
        LatLng 神岡體育園區壘球場 = new LatLng(24.341066, 120.62919999999997);
        mMap.addMarker(new MarkerOptions().position(神岡體育園區壘球場).title("神岡體育園區壘球場").snippet("臺中市大甲區東陽新村71-15號"));
        LatLng 神岡體育園區棒球場 = new LatLng(24.341066, 120.62919999999997);
        mMap.addMarker(new MarkerOptions().position(神岡體育園區棒球場).title("神岡體育園區棒球場").snippet("臺中市大甲區東陽新村71-15號"));
        LatLng 東勢體1 = new LatLng(24.341066, 120.62919999999997);
        mMap.addMarker(new MarkerOptions().position(東勢體1).title("東勢體1").snippet("臺中市大甲區東陽新村71-15號"));
        LatLng 大里足球場 = new LatLng(24.341066, 120.62919999999997);
        mMap.addMarker(new MarkerOptions().position(大里足球場).title("大里足球場").snippet("臺中市大甲區東陽新村71-15號"));
        LatLng 大雅體育園區二期 = new LatLng(24.341066, 120.62919999999997);
        mMap.addMarker(new MarkerOptions().position(大雅體育園區二期).title("大雅體育園區二期").snippet("臺中市大甲區東陽新村71-15號"));
        LatLng 同安厝多功能草皮運動場 = new LatLng(24.341066, 120.62919999999997);
        mMap.addMarker(new MarkerOptions().position(同安厝多功能草皮運動場).title("同安厝多功能草皮運動場").snippet("臺中市大甲區東陽新村71-15號"));
        LatLng 社南里籃球場 = new LatLng(24.341066, 120.62919999999997);
        mMap.addMarker(new MarkerOptions().position(社南里籃球場).title("社南里籃球場").snippet("臺中市大甲區東陽新村71-15號"));
        LatLng 臺中客家樂活園區樂活運動館 = new LatLng(24.341066, 120.62919999999997);
        mMap.addMarker(new MarkerOptions().position(臺中客家樂活園區樂活運動館).title("臺中客家樂活園區-樂活運動館").snippet("臺中市大甲區東陽新村71-15號"));
        LatLng 臺中市烏日全民運動館 = new LatLng(24.341066, 120.62919999999997);
        mMap.addMarker(new MarkerOptions().position(臺中市烏日全民運動館).title("臺中市烏日全民運動館").snippet("臺中市大甲區東陽新村71-15號"));

        LatLng taichung = new LatLng(24.163434771541002, 120.67463672003178);  //設定台中座標
        zoom = 17;
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(taichung, zoom));
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);  //一般地圖

        requestPermission();  //檢查授權

        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
    }

    private void requestPermission()
    {
        if (Build.VERSION.SDK_INT >= 23)  //androis 6.0 以上
        {
            //判斷是否已取得授權
            int hasPermission = ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);

            if (hasPermission != PackageManager.PERMISSION_GRANTED)  //未取得授權
            {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                return;
            }
        }
        setMyLocation();  //如果版本為 6.0 以下，或版本為 6.0 以上但使用者已授權，顯示定位圖層
    }

    //使用者完成授權的選擇以後，會呼叫 onRequestPermissionsResult 方法
    //第一個參數 : 請求授權代碼
    //第二個參數 : 請求的授權名稱
    //第三個參數 : 使用者選擇授權的結果
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        if (requestCode == 1)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)  //按允許鈕
            {
                setMyLocation();  //顯示定位圖層
            }
            else  //按拒絕鈕
            {
                Toast.makeText(this, "未取得授權!", Toast.LENGTH_SHORT).show();
                finish();  //結束應用程式
            }
        }
        else
        {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void setMyLocation() throws SecurityException
    {
        mMap.setMyLocationEnabled(true);  //顯示定位圖層
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        return false;
    }
}