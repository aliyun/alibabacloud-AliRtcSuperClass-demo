//
//  SmallClassGroupMemberCell.m
//  RTCSmallClass
//
//  Created by 孙震 on 2020/9/28.
//

#import "SmallClassGroupMemberCell.h"
#import "NSBundle+RTCSmallClass.h"

@interface SmallClassGroupMemberCell()

@property (unsafe_unretained, nonatomic) IBOutlet UIImageView *muteImage;
@property (unsafe_unretained, nonatomic) IBOutlet UILabel *userNameLabel;


@end

@implementation SmallClassGroupMemberCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}


- (void)setUserName:(NSString *)userName {
    _userName = userName;
    self.userNameLabel.text = userName;
}


- (void)setIsMute:(BOOL)isMute {
    _isMute = isMute;
    if (_isMute) {
        [self.muteImage setImage:[NSBundle RSC_pngImageWithName:@"mic_off"]];
    } else {
        [self.muteImage setImage:[NSBundle RSC_pngImageWithName:@"mic_on"]];
    }
}
@end
