
import oauth.signpost.*
import oauth.signpost.basic.*
import oauth.signpost.http.*
import oauth.signpost.signature.*
import oauth.signpost.exception.*

println '<pre>'
def consumer = zget('/user/consumer')
def provider = zget('/user/provider')
def authUrl = zget('/user/authurl')
def oauth_verifier = zget('/request/params/oauth_verifier')

println 'Using these details to get the access token: '
println 'Consumer token secret: ' + consumer.tokenSecret
println 'Consumer token: ' + consumer.token
println 'authUrl: ' + authUrl
println 'OAuth verifier: ' + oauth_verifier
println ''

provider.retrieveAccessToken(consumer, oauth_verifier)

zput('/storage/token', consumer.token)
zput('/storage/secret', consumer.tokenSecret)
println 'Success! Use the token and secret above to issue requests to twitter.'
println 'They have been written to /storage/token and /storage/secret for convenience.'
println "You can <a href='/'>go here to test</a>."
println '</pre>'
