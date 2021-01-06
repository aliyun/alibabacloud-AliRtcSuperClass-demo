//
//  SmallClassGroupMemberCell.h
//  RTCSmallClass
//
//  Created by 孙震 on 2020/9/28.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface SmallClassGroupMemberCell : UITableViewCell

@property (copy, nonatomic) NSString *userName;

@property (assign, nonatomic) BOOL  isMute;

@end

NS_ASSUME_NONNULL_END
