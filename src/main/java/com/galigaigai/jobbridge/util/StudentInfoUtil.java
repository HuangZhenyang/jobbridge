package com.galigaigai.jobbridge.util;

/**
 * Created by SYunk on 2018/3/20.
 * 学生详细信息处理工具类
 */
public class StudentInfoUtil {
    /*private static final String[] cities = {"北京","上海","广州","深圳","武汉","南京","成都","天津","杭州","苏州","西安","大连","其他"};
    private static final String[] industries = {"互联网","交通/物流/航天/航空","媒体/文化/休闲","能源/资源","医疗/医药","地产/建筑/施工材料",
            "多元化","奢侈品","工业/机械/化工","汽车/汽车部件","消费品","生产/制造/纺织","电子/电器/家电","通信/计算设备/软件","金融","零售/贸易",
            "粮油食品","公用事业","军工","专业服务(会计师/咨询/法律/人力资源/其他)","其他","广告/公关","教育/科研/非盈利"};
    private static final String[] functions = {"咨询/数据分析/行业研究/战略","市场与销售","运营","财务/审计/税务/融资","银行/基金/保险/证券",
            "记者/编辑/文案/策划","投资","产品","广告/公关","项目管理","人力资源","行政","技术","采购/供应链","设计","其他"};*/

    public static String[] getStudentIntention(String[] intentionDictionary, String[] intentions){
        String[] intention = new String[intentionDictionary.length];
        for(int i = 0;i < intention.length;i++){
            intention[i] = "0";
        }
        for(int i = 0;i < intentions.length;i++){
            for(int j = 0;j < intentionDictionary.length;j++){
                if(intentions[i].equals(intentionDictionary[j])){
                    intention[j] = "1";
                }
            }
        }
        return intention;
    }
}
