package com.zds.treedemo.service.impl;

import com.zds.treedemo.domain.TbLogVisit;
import com.zds.treedemo.mapper.TagCategoryMapper;
import com.zds.treedemo.mapper.TbLogVisitMapper;
import com.zds.treedemo.service.ActionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

/**
 * @author zhudongsheng
 * @Description: TODO
 * @date 2021/1/18 17:59
 */
@Service
public class ActionServiceImpl implements ActionService {

    @Autowired
    TagCategoryMapper tagCategoryMapper;

    @Autowired
    TbLogVisitMapper tbLogVisitMapper;


    @Override
    public void insertLogVisit(TbLogVisit tbLogVisit) {

    }

    /**
     * 添加日志信息入库
     *
     * @param tbLogVisit
     * @return
     */
    @Override
    public int insertLog(TbLogVisit tbLogVisit) {
        tbLogVisit.setUpdateTime(new Date());
        int count = 0;
        //如果有异常直接添加
        if (StringUtils.isNoneEmpty(tbLogVisit.getVisitThrowingErro())) {
            tbLogVisit.setCreateTime(new Date());
            count = tbLogVisitMapper.insert(tbLogVisit);
        } else {
            String visitIpAddress = tbLogVisit.getVisitIpAddress();
            String visitApi = tbLogVisit.getVisitApi();
            Example tbLogVisitExample = new Example(TbLogVisit.class);
            Example.Criteria criteria = tbLogVisitExample.createCriteria();
            criteria.andEqualTo("visitIpAddress", visitIpAddress);
            criteria.andEqualTo("visitApi", visitApi);
            List<TbLogVisit> tbLogVisits = tbLogVisitMapper.selectByExample(tbLogVisitExample);
            if (tbLogVisits != null) {
                Long nums = 0L;
                Double sums = 0D;
                for (TbLogVisit logVisit : tbLogVisits) {
                    //统计调用次数
                    Long visitNum = logVisit.getVisitNum();
                    nums = tbLogVisit.getVisitNum() + visitNum;
                    //统计耗时
                    Double visitTimeConsumingData = Double.parseDouble(logVisit.getVisitTimeConsuming());
                    Double visitTimeConsumingParam = Double.parseDouble(tbLogVisit.getVisitTimeConsuming());
                    Double sum = visitTimeConsumingData + visitTimeConsumingParam;
                    sums = sums + sum;
                }
                Double numDouble = Double.parseDouble(String.valueOf(nums));
                //统计平均耗时
                Double avg = sums / numDouble;
                tbLogVisit.setVisitTimeConsuming(avg.toString());
                tbLogVisit.setVisitNum(nums);
                count = tbLogVisitMapper.updateByExample(tbLogVisit, tbLogVisitExample);
            } else {
                tbLogVisit.setCreateTime(new Date());
                count = tbLogVisitMapper.insert(tbLogVisit);
            }
        }

        return count;
    }
}
