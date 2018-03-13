package com.galigaigai.jobbridge.util;



import com.galigaigai.jobbridge.model.Recruit;

import java.util.List;

/**
 * Created by SYunk on 2017/7/27.
 */
public class RecruitUtil {
    public static List<Recruit> orderByTime(List<Recruit> recruitList){
        for(int i = 0;i<recruitList.size() - 1;i++){
            for(int j = 0;j<recruitList.size() - i - 1;j++){
                if(recruitList.get(j).getDateTime().before(recruitList.get(j+1).getDateTime())){
                    Recruit temp = recruitList.get(j);
                    recruitList.set(j,recruitList.get(j + 1));
                    recruitList.set(j + 1,temp);
                }
            }
        }
        return recruitList;
    }
}
