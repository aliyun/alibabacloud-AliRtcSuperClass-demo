//
//  SmallClassChooseSubclassController.h
//  RTCSmallClass
//
//  Created by 孙震 on 2020/9/15.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface SmallClassChooseSubclassController : UITableViewController

- (instancetype)initWithDefaultIndex:(NSInteger)index
                            complete:(void(^)(NSInteger index,NSString *className))completeBlock;

@end

NS_ASSUME_NONNULL_END
