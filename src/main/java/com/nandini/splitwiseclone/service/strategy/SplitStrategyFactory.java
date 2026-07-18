package com.nandini.splitwiseclone.service.strategy;

import com.nandini.splitwiseclone.enums.SplitType;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class SplitStrategyFactory {

    private final List<SplitStrategy> splitStrategies;

    public SplitStrategyFactory(List<SplitStrategy> splitStrategies){
        this.splitStrategies = splitStrategies;
    }

    public SplitStrategy getStrategy(SplitType splitType){

        for(SplitStrategy strategy: splitStrategies){

            if(strategy.getSupportedSplitType() == splitType){
                return strategy;
            }
        }

        throw new IllegalArgumentException(
                "No strategy found for split type: " + splitType
        );
    }

}
