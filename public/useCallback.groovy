import oauth.signpost.*
import oauth.signpost.basic.*
import oauth.signpost.http.*
import oauth.signpost.signature.*
import oauth.signpost.exception.*
import zero.core.utils.URIUtils

//Consumer key & secret from dev.twitter.com are stored in zero.config
OAuthConsumer consumer = new DefaultOAuthConsumer(
		zget('/config/twitter/consumerKey'),
		zget('/config/twitter/consumerSecret'))

OAuthProvider provider = new DefaultOAuthProvider(
		"http://twitter.com/oauth/request_token",
		"http://twitter.com/oauth/access_token",
		"http://twitter.com/oauth/authorize")

println "<pre>"
println "Fetching consumer request tokens..."

def callbackUri = URIUtils.getAbsoluteUri('/verify.groovy')
def authUrl = provider.retrieveRequestToken(consumer, callbackUri)

zput("/user/consumer", consumer)
zput("/user/provider", provider)
zput("/user/authurl", authUrl)

println "Consumer object: " + consumer
println "Consumer token secret: " + consumer.tokenSecret
println "Consumer token: " + consumer.token
println ""
println "Go here and authorise the app for your account: "
println "<a href='${authUrl}'>${authUrl}</a>"
println '</pre>'