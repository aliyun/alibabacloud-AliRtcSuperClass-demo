//
//  SmallClassSeatsLoaderFactory.m
//  Pods
//
//  Created by Aliyun on 2020/7/22.
//

#import "SmallClassFetcherFactory.h"
#import "SmallClassFether.h"

@implementation SmallClassFetcherFactory

+ (id<SmallClassFetcherProtocal>)getFetcher:(NSString *)type
{
    if ([type isEqualToString:KSmallClassFetherDefault])
    {
        return [[SmallClassFether alloc]init];
    }
    
    return nil;
}
@end
