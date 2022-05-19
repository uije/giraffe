package net.alhazmy13.wordcloud;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.reactivex.rxjava3.subjects.BehaviorSubject;
import io.reactivex.rxjava3.subjects.Subject;

/**
 * The type Word cloud view.
 */
public class WordCloudView extends WebView {
    private final float displayDensity;
    private Context mContext;
    private List<WordCloud> dataSet;
    private int old_min;
    private int old_max;
    private int[] colors;
    private Random random;
    private int viewPortHeight;
    private int viewPortWidth;
    private int max;
    private int min;
    Subject<Boolean> onMeasuredSubject = BehaviorSubject.create();
    private LoadListener loadListener;

    /**
     * Instantiates a new Word cloud view.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public WordCloudView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        this.dataSet = new ArrayList<>();
        this.viewPortHeight = 300;
        this.viewPortWidth = 450;
        this.max = 70;
        this.min = 15;
        this.colors = new int[0];
        this.random = new Random();
        this.displayDensity = context.getResources().getDisplayMetrics().density;
        setVerticalScrollBarEnabled(false);
        setHorizontalScrollBarEnabled(false);
        setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                if (loadListener != null) {
                    loadListener.onLoadFinished();
                }
            }
        });
    }

    /**
     * Init.
     */
    @SuppressLint({"AddJavascriptInterface", "SetJavaScriptEnabled"})
    void init() {
        Log.d(this.getClass().getSimpleName(), String.format("init vp(%s %s)", viewPortWidth, viewPortHeight));

        JavascriptInterface myJavascriptInterface = new JavascriptInterface(mContext);

        myJavascriptInterface
                .setCloudParams("", getData(), "FreeSans", viewPortWidth, viewPortHeight);
        addJavascriptInterface(myJavascriptInterface, "jsinterface");
        WebSettings webSettings = getSettings();
        webSettings.setBuiltInZoomControls(false);
        webSettings.setSupportZoom(true);
        webSettings.setJavaScriptEnabled(true);

        // Use HTML5 localstorage to maintain app state
        webSettings.setDefaultTextEncodingName("utf-8");
        webSettings.setAppCacheEnabled(false);
        webSettings.setAllowFileAccess(false);

        // 컨텐츠가 웹뷰보다 클 경우 스크린 크기에 맞게 조정
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setUserAgentString("Android");
        loadUrl("file:///android_asset/wordcloud.html");
    }

    /**
     * Sets data set.
     *
     * @param dataSet the data set
     */
    public void setDataSet(List<WordCloud> dataSet) {
        this.dataSet = dataSet;
    }


    /**
     * Notify data set changed.
     */
    public void notifyDataSetChanged() {
        updateMaxMinValues();
        onMeasuredSubject
                .firstOrError()
                .subscribe(pair -> init());
    }

    /**
     * Gets data.
     *
     * @return the data
     */
    public String getData() {

        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < dataSet.size(); i++) {
            sb.append("{\"word\":\"").append(dataSet.get(i).getText());
            sb.append("\",\"size\":\"").append(scale(dataSet.get(i).getWeight()));
            sb.append("\",\"color\":\"");
            sb.append(getColor()).append("\"}");
            if (i < dataSet.size() - 1) {
                sb.append(",");
            }
        }
        sb.append("]");
        return sb.toString();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        this.setMeasuredDimension(width, height);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d(this.getClass().getSimpleName(), String.format("onMeasure px(%s %s) vp(%s %s)", width, height, viewPortWidth, viewPortHeight));
        //중간에 값이 0인 데이터가 들어와서 필터링
        if (width != 0 && height != 0) {
            setSize(width, height);
            onMeasuredSubject.onNext(true);
        }
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        setSize(w, h);
        super.onSizeChanged(w, h, oldw, oldh);
        Log.d(this.getClass().getSimpleName(), String.format("onSizeChanged px(%s %s) vp(%s %s)", w, h, viewPortWidth, viewPortHeight));
    }

    private float scale(int inputY) {
        float x = inputY - old_min;
        float y = old_max - old_min;
        float percent = (y == 0 ? 0.5f : x / y);
        return percent * (max - min) + min;
    }

    private void updateMaxMinValues() {
        old_min = Integer.MAX_VALUE;
        old_max = Integer.MIN_VALUE;
        for (WordCloud wordCloud : dataSet) {
            if (wordCloud.getWeight() < old_min) {
                old_min = wordCloud.getWeight();
            }
            if (wordCloud.getWeight() > old_max) {
                old_max = wordCloud.getWeight();
            }
        }
    }

    public void setColors(int[] colors) {
        this.colors = colors;
    }

    private String getColor() {
        if (colors.length == 0)
            return "0";
        return "#" + Integer.toHexString(colors[random.nextInt(colors.length - 1)]).substring(2);
    }

    /**
     * View size.
     *
     * @param width  the width px
     * @param height the height px
     */
    public void setSize(int width, int height) {
        viewPortWidth = (int) (width / displayDensity);
        viewPortHeight = (int) (height / displayDensity);
    }

    public void setScale(int max, int min) {
        if (min > max) {
            throw new RuntimeException("MIN scale cannot be larger than MAX");
        }
        this.max = max;
        this.min = min;
    }

    public void setOnLoadListener(WordCloudView.LoadListener loadListener) {
        this.loadListener = loadListener;
    }

    public interface LoadListener {
        void onLoadFinished();
    }
}
