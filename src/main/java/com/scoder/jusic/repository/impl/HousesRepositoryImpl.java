package com.scoder.jusic.repository.impl;

import com.scoder.jusic.configuration.JusicProperties;
import com.scoder.jusic.model.House;
import com.scoder.jusic.repository.HousesRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author JumpAlang
 * @create 2020-06-21 22:18
 */
@Repository
public class HousesRepositoryImpl implements HousesRespository {

    @Autowired
    private JusicProperties jusicProperties;
    @Autowired
    private JusicProperties.RedisKeys redisKeys;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public Boolean destroy(List houses) {
        this.reset();
        this.rightPushAll(houses.toArray());
        return true;
    }

    @Override
    public CopyOnWriteArrayList<House> initialize() {
        if(this.size() == 0){
            House house = new House();
            house.setEnableStatus(true);
            house.setName(JusicProperties.HOUSE_DEFAULT_NAME);
            house.setId(JusicProperties.HOUSE_DEFAULT_ID);
            house.setDesc(JusicProperties.HOUSE_DEFAULT_DESC);
            house.setCreateTime(System.currentTimeMillis());
            house.setNeedPwd(false);
            house.setRemoteAddress("127.0.0.1");
            CopyOnWriteArrayList<House> houses = new CopyOnWriteArrayList<House>();
            houses.add(house);
            return houses;
        }else{
            return this.get();
        }
    }

    @Override
    public Long add(Object... value) {
        return redisTemplate.opsForList()
                .rightPush(redisKeys.getHouses(), value);
    }


    @Override
    public Long size() {
        return redisTemplate.opsForList()
                .size(redisKeys.getHouses());
    }

    @Override
    public void reset() {
        redisTemplate.opsForList()
                .trim(redisKeys.getHouses(), 1, 0);
    }

    @Override
    public Long rightPushAll(Object... value) {
        return redisTemplate.opsForList()
                .rightPushAll(redisKeys.getHouses(), value);
    }

    @Override
    public CopyOnWriteArrayList<House> get() {
        Long size = this.size();
        size = size == null ? 0 : size;
        CopyOnWriteArrayList<House> houses = new CopyOnWriteArrayList<>();

        List<House> houseOrigin = (List<House>) redisTemplate.opsForList()
                .range(redisKeys.getHouses(), 0, size);
        for(House house : houseOrigin){
            houses.add(house);
        }
        return houses;
    }
}
