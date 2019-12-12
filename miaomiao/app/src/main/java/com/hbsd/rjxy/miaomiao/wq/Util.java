package com.qiqi.miao_3;

/**
 * @author DingChao
 *         2017/2/10
 */

public class Util {
    private static String[] nameArray = new String[]{
            "Windows", "Mac", "Linux"
    };
    private static String[] contentArray = new String[]{
            "在动作类影片中，只要发生混乱，那么绝对就有木仓战。现在的技术越来越发达，电影或电视中的特效也做的越来越逼真，演员们被木仓打中的效果也很形象，我们经常能看到被木仓打中的伤口血淋林的暴露在大屏幕中，从演员的表演中我们能看到木仓击是很痛的，那么你们有想过被木仓打中到底会有多痛？什么感觉吗？网站有网友为我们分享被子弹打中的感觉\n" +
                    "1、“老实说，比我想象中的感觉要轻很多。本来我以为很痛，可是被打中后就像是被棒球击中的感觉一样，刚开始的几秒钟没什么知觉，过会才感到痛\n" +
                    "2、“被子弹打到的感觉就像是一直有人拿针扎你一样，刺痛刺痛的。”\n" +
                    "3、“我当初大腿被木仓击中，子弹直接从我的大腿中传过去，连带着我的肌腱也被击中，那种感觉我觉得用疼痛两个字已经不足以形容了\n" +
                    "4、“在我十七岁的时候，脚被木仓击中，当时我以为是被蜜蜂蛰了，因为仿佛听到了蜜蜂的声音，没过几秒钟，脚上就传来灼热感，这才知道原来是被木仓击中了。\n" +
                    "5、“我只是听到的木仓声，却没有意识到自己中木仓了。直到血流出来才意识到。所以，对我来讲，被子弹击中没什么感觉。"
            ,
            "GNOME or KDE desktop\n" +
                    " processor with support for AMD Virtualization™ (AMD-V™)"


    };

    /**
     * 获取文本内容根据下标
     *
     * @param position
     * @return
     */

    public static String getContent(int position) {
        return contentArray[position % contentArray.length];
    }

    /**
     * 获取名称根据下标
     *
     * @param position
     * @return
     */
    public static String getName(int position) {
        return nameArray[position % contentArray.length];
    }
}

