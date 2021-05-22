
LemurClientConfig {
  classvar <>defaultLemurClientRemoteIP=nil;

	*setDefaultLemurClientRemoteIP {
	arg remoteIP;
		defaultLemurClientRemoteIP = remoteIP;
		postln("The default lemur client remote IP is set to " + remoteIP);
	}
}
