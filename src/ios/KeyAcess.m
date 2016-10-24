/********* KeyAcess.m Cordova Plugin Implementation *******/
#import "KeyAcess.h"
#define SERVICE_NAME @"keyData"
#define GROUP_NAME @"com.pankanis.anandrathi.scoreChip.com.apps.shared"


@implementation KeyAcess

#pragma mark- callback methods
- (void)storeMethod:(CDVInvokedUrlCommand*)command
{
    keychain  =[[Keychain alloc] initWithService:SERVICE_NAME withGroup:nil];
    CDVPluginResult* pluginResult = nil;
    NSString* echo = [command.arguments objectAtIndex:0];
    NSString *keyForVal=[command.arguments objectAtIndex:1];
    if (echo != nil && [echo length] > 0) {
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:echo];
        [self storeData:keyForVal data:echo];
    } else {
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR];
    }
    
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

- (void)getMethod:(CDVInvokedUrlCommand*)command
{
    keychain  =[[Keychain alloc] initWithService:SERVICE_NAME withGroup:nil];
    CDVPluginResult* pluginResult = nil;
    NSString *keyForVal=[command.arguments objectAtIndex:0];
    if (keyForVal != nil && [keyForVal length] > 0) {
        NSString *resultString=[self fetchData:keyForVal];
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:resultString];
    }else{
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR];
    }
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

- (void)deleteMethod:(CDVInvokedUrlCommand*)command
{
    keychain  =[[Keychain alloc] initWithService:SERVICE_NAME withGroup:nil];
    CDVPluginResult* pluginResult = nil;
    NSString *keyForVal=[command.arguments objectAtIndex:0];
    
    if (keyForVal != nil && [keyForVal length] > 0) {
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:keyForVal];
        [self removeData:keyForVal];
    } else {
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR];
    }
    
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}
#pragma mark - generate private public key
- (void)geneKeyPair:(CDVInvokedUrlCommand*)command
{
    CDVPluginResult* pluginResult = nil;
    
    NSMutableArray *value=[self generateKeysExample];
    NSMutableDictionary *keyPair=[[NSMutableDictionary alloc]init];
    [keyPair setValue:[value objectAtIndex:0] forKey:@"PublicKey"];
    [keyPair setValue:[value objectAtIndex:1] forKey:@"PrivateKey"];
    NSLog(@"dictionary is %@",keyPair);
   // NSString *string=[NSString stringWithFormat:@"Public Key:\n%@\n\nPrivate Key:\n%@",[value objectAtIndex:0],[value objectAtIndex:1]];
   // pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:string];
    pluginResult=[CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:keyPair];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}
#pragma mark- signature
- (void)geneSigning:(CDVInvokedUrlCommand*)command
{
    CDVPluginResult* pluginResult = nil;
    NSString* strToenc = [command.arguments objectAtIndex:0];
    NSString *keyForSign=[command.arguments objectAtIndex:1];
    NSString *string=[self signing:strToenc publicKey:keyForSign];
    pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:string];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

#pragma mark - keyChain methods
-(void)storeData:(NSString *)keyForVal data:(NSString *)storeVal{
    NSString *key =keyForVal;
    NSData * value = [storeVal dataUsingEncoding:NSUTF8StringEncoding];
    
    if([keychain insert:key :value])
    {
        NSLog(@"Successfully added data");
    }
    else
        NSLog(@"Failed to  add data");
}

-(NSString *)fetchData :(NSString *)keyForVal{
    NSString *key= keyForVal;
    NSData * data =[keychain find:key];
    NSString *fetchString;
    if(data == nil)
    {
        NSLog(@"Keychain data not found");
    }
    else
    {
        fetchString=[[NSString alloc] initWithData:data encoding:NSUTF8StringEncoding];
    }
    return fetchString;
}

-(void)removeData :(NSString *)keyForVal{
    NSString *key =keyForVal;
    if([keychain remove:key])
    {
        NSLog(@"Successfully removed data");
    }
    else
    {
        NSLog(@"Unable to remove data");
    }
}

#pragma mark - generate public private key
- (NSMutableArray *)generateKeysExample
{
    error = [[BDError alloc] init];
    
    NSMutableArray *keyArray=[NSMutableArray array];
    
    RSACryptor = [[BDRSACryptor alloc] init];
    RSAKeyPair = [RSACryptor generateKeyPairWithKeyIdentifier:@"abc.com.da"
                                                        error:error];
    
    [keyArray addObject:RSAKeyPair.publicKey];
    [keyArray addObject:RSAKeyPair.privateKey];
    return keyArray;
}
-(NSString *)signing :(NSString *)strTosign publicKey:(NSString *)publicKey{
    
    
    NSString *signature=[RSACryptor encrypt:strTosign key:publicKey error:error];
    return  signature;
}

@end
