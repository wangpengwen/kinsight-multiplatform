//
//  HomeViewList.swift
//  KotlinIOS
//
//  Created by Dmitri 222 on 10/11/19.


import Foundation
import SwiftUI
import SharedCode

struct HomeViewList: View {
    @ObservedObject var ideaViewModel = IdeasViewModel(repository: IdeaRepository())
    
    var body: some View {
       
             ZStack{
               VStack{
                    ActivityIndicator(isAnimating: $ideaViewModel.dataRequestInProgress)
                                       
                                  
                }.zIndex(1)
                NavigationView{
               VStack {
                    Text(CommonKt.createApplicationScreenMessage())
                    List(ideaViewModel.ideas){
                        idea in
                        NavigationLink(destination: IdeaView(ideaModel: idea.ideaModel)) {
                           
                            HomeViewListRow(ideaModel: idea.ideaModel)
                        }
                      
                    }
               }.zIndex(0)
                }.navigationBarHidden(true)
                .navigationBarTitle("")
               
            }
       
    }
}
  
/*
 //NavigationLink(destination: IdeaView(ideaModel: idea)) {
                    HomeViewListRowNative(ideaModel: idea)
               // }
 */
 
