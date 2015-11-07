package com.danielasfregola.twitter4s.http.clients.trends

import scala.concurrent.Future

import com.danielasfregola.twitter4s.entities.{Location, LocationTrends}
import com.danielasfregola.twitter4s.http.clients.OAuthClient
import com.danielasfregola.twitter4s.http.clients.trends.parameters.{LocationParameters, TrendsParameters}
import com.danielasfregola.twitter4s.util.Configurations

trait TwitterTrendClient extends OAuthClient with Configurations {

  private val trendsUrl = s"$apiTwitterUrl/$twitterVersion/trends"

  def globalTrends(exclude_hashtags: Boolean = false): Future[LocationTrends] = trends(1, exclude_hashtags)

  def trends(woeid: Long, exclude_hashtags: Boolean = false): Future[LocationTrends] = {
    val exclude = if (exclude_hashtags) Some("hashtags") else None
    val parameters = TrendsParameters(woeid, exclude)
    Get(s"$trendsUrl/place.json", parameters).respondAs[LocationTrends]
  }

  def locationTrends(): Future[Seq[Location]] =
    Get(s"$trendsUrl/available.json").respondAs[Seq[Location]]

  def closestLocationTrends(latitude: Double, longitude: Double): Future[Seq[Location]] = {
    val parameters = LocationParameters(latitude, longitude)
    Get(s"$trendsUrl/closest.json", parameters).respondAs[Seq[Location]]
  }

}