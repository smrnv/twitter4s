package twitter4s.http.clients.lists

import spray.http.HttpMethods
import spray.http.Uri.Query
import twitter4s.entities.{User, Subscription, Subscriptions, Tweet}
import twitter4s.util.{ClientSpec, ClientSpecContext}

class TwitterListClientSpec  extends ClientSpec {

  trait TwitterListClientSpecContext extends ClientSpecContext with TwitterListClient

  "Twitter List Client" should {

    "get lists for a user" in new TwitterListClientSpecContext {
      val result: Seq[Subscription] = when(lists("DanielaSfregola")).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/list.json"
        request.uri.query === Query("reverse=false&screen_name=DanielaSfregola")
      }.respondWith("/twitter/lists/lists.json").await
      result === loadJsonAs[Seq[Subscription]]("/fixtures/lists/lists.json")
    }

    "get lists for a user by id" in new TwitterListClientSpecContext {
      val result: Seq[Subscription] = when(listsPerUserId(2911461333L)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/list.json"
        request.uri.query === Query("reverse=false&user_id=2911461333")
      }.respondWith("/twitter/lists/lists.json").await
      result === loadJsonAs[Seq[Subscription]]("/fixtures/lists/lists.json")
    }

    "get a list timeline by id" in new TwitterListClientSpecContext {
      val result: Seq[Tweet] = when(listTimelinePerListId(8044403)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/statuses.json"
        request.uri.query === Query("count=20&include_entities=true&include_rts=false&list_id=8044403")
      }.respondWith("/twitter/lists/list_timeline.json").await
      result === loadJsonAs[Seq[Tweet]]("/fixtures/lists/list_timeline.json")
    }

    "get a list timeline by slug and owner" in new TwitterListClientSpecContext {
      val result: Seq[Tweet] = when(listTimelinePerSlugAndOwner("meetup-20100301", "twitterapi")).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/statuses.json"
        request.uri.query === Query("count=20&include_entities=true&include_rts=false&owner_screen_name=twitterapi&slug=meetup-20100301")
      }.respondWith("/twitter/lists/list_timeline.json").await
      result === loadJsonAs[Seq[Tweet]]("/fixtures/lists/list_timeline.json")
    }

    "get a list timeline by slug and owner id" in new TwitterListClientSpecContext {
      val result: Seq[Tweet] = when(listTimelinePerSlugAndOwnerId("meetup-20100301", 6253282)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/statuses.json"
        request.uri.query === Query("count=20&include_entities=true&include_rts=false&owner_id=6253282&slug=meetup-20100301")
      }.respondWith("/twitter/lists/list_timeline.json").await
      result === loadJsonAs[Seq[Tweet]]("/fixtures/lists/list_timeline.json")
    }

    "remove list member by list id and user" in new TwitterListClientSpecContext {
      val result: Unit = when(removeListMemberPerListId(8044403, "DanielaSfregola")).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/members/destroy.json"
        request.uri.query === Query("list_id=8044403&screen_name=DanielaSfregola")
      }.respondWithOk.await
      result === ()
    }

    "remove list member by slug and owner and user" in new TwitterListClientSpecContext {
      val result: Unit = when(removeListMemberPerSlugAndOwner("meetup-20100301", "twitterapi", "DanielaSfregola")).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/members/destroy.json"
        request.uri.query === Query("owner_screen_name=twitterapi&screen_name=DanielaSfregola&slug=meetup-20100301")
      }.respondWithOk.await
      result === ()
    }

    "remove list member by slug and owner id and user" in new TwitterListClientSpecContext {
      val result: Unit = when(removeListMemberPerSlugAndOwnerId("meetup-20100301", 6253282, "DanielaSfregola")).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/members/destroy.json"
        request.uri.query === Query("owner_id=6253282&screen_name=DanielaSfregola&slug=meetup-20100301")
      }.respondWithOk.await
      result === ()
    }

    "remove list member by list id and user id" in new TwitterListClientSpecContext {
      val result: Unit = when(removeListMemberIdPerListId(8044403, 2911461333L)).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/members/destroy.json"
        request.uri.query === Query("list_id=8044403&user_id=2911461333")
      }.respondWithOk.await
      result === ()
    }

    "remove list member by slug and owner and user id" in new TwitterListClientSpecContext {
      val result: Unit = when(removeListMemberIdPerSlugAndOwner("meetup-20100301", "twitterapi", 2911461333L)).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/members/destroy.json"
        request.uri.query === Query("owner_screen_name=twitterapi&slug=meetup-20100301&user_id=2911461333")
      }.respondWithOk.await
      result === ()
    }

    "remove list member by slug and owner id and user id" in new TwitterListClientSpecContext {
      val result: Unit = when(removeListMemberIdPerSlugAndOwnerId("meetup-20100301", 6253282, 2911461333L)).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/members/destroy.json"
        request.uri.query === Query("owner_id=6253282&slug=meetup-20100301&user_id=2911461333")
      }.respondWithOk.await
      result === ()
    }

    "get list memberships per user" in new TwitterListClientSpecContext {
      val result: Subscriptions = when(memberships("DanielaSfregola")).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/memberships.json"
        request.uri.query === Query("count=20&cursor=-1&filter_to_owned_lists=false&screen_name=DanielaSfregola")
      }.respondWith("/twitter/lists/list_memberships.json").await
      result === loadJsonAs[Subscriptions]("/fixtures/lists/list_memberships.json")
    }

    "get list memberships per user id" in new TwitterListClientSpecContext {
      val result: Subscriptions = when(membershipsPerUserId(2911461333L)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/memberships.json"
        request.uri.query === Query("count=20&cursor=-1&filter_to_owned_lists=false&user_id=2911461333")
      }.respondWith("/twitter/lists/list_memberships.json").await
      result === loadJsonAs[Subscriptions]("/fixtures/lists/list_memberships.json")
    }

    "add members ids per list id" in new TwitterListClientSpecContext {
      val result: Unit = when(addMembersIdsPerListId(8044403, Seq(2911461333L, 2911461334L))).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/members/create_all.json"
        request.uri.query === Query("list_id=8044403&user_id=2911461333,2911461334")
      }.respondWithOk.await
      result === ()
    }
    
    "add members ids per slug and owner" in new TwitterListClientSpecContext {
      val result: Unit = when(addMembersIdsPerSlugAndOwner("meetup-20100301", "twitterapi", Seq(2911461333L, 2911461334L))).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/members/create_all.json"
        request.uri.query === Query("owner_screen_name=twitterapi&slug=meetup-20100301&user_id=2911461333,2911461334")
      }.respondWithOk.await
      result === ()
    }
    
    "add members ids per slug and owner id" in new TwitterListClientSpecContext {
      val result: Unit = when(addMembersIdsPerSlugAndOwnerId("meetup-20100301", 6253282, Seq(2911461333L, 2911461334L))).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/members/create_all.json"
        request.uri.query === Query("owner_id=6253282&slug=meetup-20100301&user_id=2911461333,2911461334")
      }.respondWithOk.await
      result === ()
    }
    
    "add members per list id" in new TwitterListClientSpecContext {
      val result: Unit = when(addMembersPerListId(8044403, Seq("marcobonzanini", "odersky"))).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/members/create_all.json"
        request.uri.query === Query("list_id=8044403&screen_name=marcobonzanini,odersky")
      }.respondWithOk.await
      result === ()
    }
    
    "add members per slug and owner" in new TwitterListClientSpecContext {
      val result: Unit = when(addMembersPerSlugAndOwner("meetup-20100301", "twitterapi", Seq("marcobonzanini", "odersky"))).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/members/create_all.json"
        request.uri.query === Query("owner_screen_name=twitterapi&screen_name=marcobonzanini,odersky&slug=meetup-20100301")
      }.respondWithOk.await
      result === ()
    }
    
    "add members per slug and owner id" in new TwitterListClientSpecContext {
      val result: Unit = when(addMembersPerSlugAndOwnerId("meetup-20100301", 6253282, Seq("marcobonzanini", "odersky"))).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/members/create_all.json"
        request.uri.query === Query("owner_id=6253282&screen_name=marcobonzanini,odersky&slug=meetup-20100301")
      }.respondWithOk.await
      result === ()
    }
    
    "reject 'addMembersIdsPerListId' if no ids are provided" in new TwitterListClientSpecContext {
      addMembersIdsPerListId(8044403, Seq.empty) must throwA[IllegalArgumentException]("requirement failed: please, provide at least one user id")
    }
    
    "reject 'addMembersIdsPerSlugAndOwner' if no ids are provided" in new TwitterListClientSpecContext {
      addMembersIdsPerSlugAndOwner("meetup-20100301", "twitterapi", Seq.empty) must throwA[IllegalArgumentException]("requirement failed: please, provide at least one user id")
    }
    
    "reject 'addMembersIdsPerSlugAndOwnerId' if no ids are provided" in new TwitterListClientSpecContext {
      addMembersIdsPerSlugAndOwnerId("meetup-20100301", 6253282, Seq.empty) must throwA[IllegalArgumentException]("requirement failed: please, provide at least one user id")
    }
    
    "reject 'addMembersPerListId' if no screen names are provided" in new TwitterListClientSpecContext {
      addMembersPerListId(8044403, Seq.empty) must throwA[IllegalArgumentException]("requirement failed: please, provide at least one screen name")
    }
    
    "reject 'addMembersPerSlugAndOwner' if no screen names are provided" in new TwitterListClientSpecContext {
      addMembersPerSlugAndOwner("meetup-20100301", "twitterapi", Seq.empty) must throwA[IllegalArgumentException]("requirement failed: please, provide at least one screen name")
    }
    
    "reject 'addMembersPerSlugAndOwnerId' if no screen names are provided" in new TwitterListClientSpecContext {
      addMembersPerSlugAndOwnerId("meetup-20100301", 6253282, Seq.empty) must throwA[IllegalArgumentException]("requirement failed: please, provide at least one screen name")
    }

    "get member by id per list id" in new TwitterListClientSpecContext {
      val result: User = when(memberByIdPerListId(8044403, 2911461333L)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/members/show.json"
        request.uri.query === Query("include_entities=true&list_id=8044403&skip_status=false&user_id=2911461333")
      }.respondWith("/twitter/lists/list_member.json").await
      result === loadJsonAs[User]("/fixtures/lists/list_member.json")
    }

    "get member by id per slug and owner" in new TwitterListClientSpecContext {
      val result: User = when(memberByIdPerSlugAndOwner("meetup-20100301", "twitterapi", 2911461333L)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/members/show.json"
        request.uri.query === Query("include_entities=true&owner_screen_name=twitterapi&skip_status=false&slug=meetup-20100301&user_id=2911461333")
      }.respondWith("/twitter/lists/list_member.json").await
      result === loadJsonAs[User]("/fixtures/lists/list_member.json")
    }

    "get member by id per slug and owner id" in new TwitterListClientSpecContext {
      val result: User = when(memberByIdPerSlugAndOwnerId("meetup-20100301", 6253282, 2911461333L)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/members/show.json"
        request.uri.query === Query("include_entities=true&owner_id=6253282&skip_status=false&slug=meetup-20100301&user_id=2911461333")
      }.respondWith("/twitter/lists/list_member.json").await
      result === loadJsonAs[User]("/fixtures/lists/list_member.json")
    }

    "get member per list id" in new TwitterListClientSpecContext {
      val result: User = when(memberPerListId(8044403, "DanielaSfregola")).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/members/show.json"
        request.uri.query === Query("include_entities=true&list_id=8044403&screen_name=DanielaSfregola&skip_status=false")
      }.respondWith("/twitter/lists/list_member.json").await
      result === loadJsonAs[User]("/fixtures/lists/list_member.json")
    }

    "get member per slug and owner" in new TwitterListClientSpecContext {
      val result: User = when(memberPerSlugAndOwner("meetup-20100301", "twitterapi", "DanielaSfregola")).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/members/show.json"
        request.uri.query === Query("include_entities=true&owner_screen_name=twitterapi&screen_name=DanielaSfregola&skip_status=false&slug=meetup-20100301")
      }.respondWith("/twitter/lists/list_member.json").await
      result === loadJsonAs[User]("/fixtures/lists/list_member.json")
    }

    "get member per slug and owner id" in new TwitterListClientSpecContext {
      val result: User = when(memberPerSlugAndOwnerId("meetup-20100301", 6253282, "DanielaSfregola")).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/members/show.json"
        request.uri.query === Query("include_entities=true&owner_id=6253282&screen_name=DanielaSfregola&skip_status=false&slug=meetup-20100301")
      }.respondWith("/twitter/lists/list_member.json").await
      result === loadJsonAs[User]("/fixtures/lists/list_member.json")
    }
  }

}
