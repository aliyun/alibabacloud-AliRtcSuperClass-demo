//
//  NSBundle+RTCSmallClass.m
//  Pods
//
//  Created by aliyun on 2020/8/6.
//

#import "NSBundle+RTCSmallClass.h"
#import "RTCSmallClass.h"

#import "NSBundle+RTCCommonView.h"

@implementation NSBundle (RTCSmallClass)

+ (instancetype)RTCSmallClassBundle
{
    static NSBundle *bundel = nil;
    if (bundel == nil) {
        NSString *bundlePath = [[NSBundle bundleForClass:[RTCSmallClass class]] pathForResource:@"RTCSmallClass" ofType:@"bundle"];
        bundel = [NSBundle bundleWithPath:bundlePath];
    }
    return bundel;
}

+ (UIImage *)RSC_imageWithName:(NSString *)name type:(NSString *)type
{
    int scale = [[UIScreen mainScreen] scale] <= 2 ? 2 : 3;
    NSString *fullName = [NSString stringWithFormat:@"%@@%dx",name,scale];
    NSString *path =  [[NSBundle RTCSmallClassBundle] pathForResource:fullName ofType:type];
    UIImage *image = [UIImage imageNamed:path];
    //如果不存在 则直接加载name.type
    if (!image) {
        path =  [[NSBundle RTCCommonViewBundle] pathForResource:name ofType:type];
        image = [UIImage imageNamed:path];
    }
    return image;
}

+ (UIImage *)RSC_pngImageWithName:(NSString *)name
{
    UIImage *image = [NSBundle  RSC_imageWithName:name type:@"png"];
    //从commonView中查找
    if (!image) {
        image = [NSBundle RCV_pngImageWithName:name];
    }
    return image;
}

+ (UIStoryboard *)RSC_storyboard
{
    return [UIStoryboard storyboardWithName:@"RTCSmallClass"
                                     bundle:[NSBundle bundleForClass:[RTCSmallClass class]]];
}

+ (NSString *)RSC_musicPathForResource:(NSString *)name {
    return [NSBundle RCV_pathForResource:name];
}

@end
