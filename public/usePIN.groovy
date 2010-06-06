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
def authUrl = provider.retrieveRequestToken(consumer, OAuth.OUT_OF_BAND)

zput("/user/consumer", consumer)
zput("/user/provider", provider)
zput("/user/authurl", authUrl)

println "Consumer object: " + consumer
println "Consumer token secret: " + consumer.tokenSecret
println "Consumer token: " + consumer.token
println ""
println "Go here in a new tab, authorise the app for your account, and copy the PIN: "
println "<a  target='_new' href='${authUrl}'>${authUrl}</a>"
println ""
println "When you're done, come back to this tab, paste the PIN and submit:</pre>"
println '<form action="verify.groovy" method="get">'
println 'PIN: <input type="text" name="oauth_verifier" /><br />'
println '<input type="submit" value="Submit" />'
println '</form>'
println '</pre>'