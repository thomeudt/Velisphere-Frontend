package com.velisphere.tigerspice.client.locator.maps;

/*
 * #%L
 * GWT Maps API V3 - Showcase
 * %%
 * Copyright (C) 2011 - 2012 GWT Maps API V3
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.google.gwt.ajaxloader.client.ArrayHelper;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.maps.client.MapOptions;
import com.google.gwt.maps.client.MapTypeId;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.base.LatLng;
import com.google.gwt.maps.client.events.tiles.TilesLoadedMapEvent;
import com.google.gwt.maps.client.events.tiles.TilesLoadedMapHandler;
import com.google.gwt.maps.client.geometrylib.EncodingUtils;
import com.google.gwt.maps.client.layers.TransitLayer;
import com.google.gwt.maps.client.visualizationlib.HeatMapLayer;
import com.google.gwt.maps.client.visualizationlib.HeatMapLayerOptions;
import com.google.gwt.maps.client.visualizationlib.WeightedLocation;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * See <a href=
 * "https://developers.google.com/maps/documentation/javascript/layers#JSHeatMaps"
 * >HeatMapLayer API Doc</a>
 */
public class HeatMapLayerWidget extends Composite {

  private final VerticalPanel pWidget;
  private MapWidget mapWidget;

  public HeatMapLayerWidget() {
    pWidget = new VerticalPanel();
    initWidget(pWidget);

    draw();
  }

  private void draw() {

    pWidget.clear();
    pWidget
        .add(new HTML(
            "<br>HeatMap Layers Example:<br>3981 NYC Homicides 2003-2011 (<a href='http://graphics8.nytimes.com/packages/xml/map_feed_incidents.txt?c=1906'>source NYTimes</a>)"));

    drawMap();
  }

  private void drawMap() {
    // zoom out for the clouds
    LatLng center = LatLng.newInstance(40.74, -73.94);
    MapOptions opts = MapOptions.newInstance();
    opts.setZoom(11);
    opts.setCenter(center);
    opts.setMapTypeId(MapTypeId.TERRAIN);

    mapWidget = new MapWidget(opts);
    pWidget.add(mapWidget);
    mapWidget.setSize("700px", "350px");
    mapWidget.setStyleName("map_canvas");
    
    mapWidget.addTilesLoadedHandler(new TilesLoadedMapHandler() {
        public void onEvent(TilesLoadedMapEvent event) {
          // Load something after the tiles load
      	  
      	  mapWidget.triggerResize();
      	  mapWidget.setZoom(mapWidget.getZoom());
      	  
        }
      });
    
    // show transit layer
    TransitLayer transitLayer = TransitLayer.newInstance();
    transitLayer.setMap(mapWidget);

    // create layer
    HeatMapLayerOptions options = HeatMapLayerOptions.newInstance();
    options.setOpacity(0.9);
    options.setRadius(5);
    options.setGradient(getSampleGradient());
    options.setMaxIntensity(3);
    options.setMap(mapWidget);

    HeatMapLayer heatMapLayer = HeatMapLayer.newInstance(options);
    // set data
    JsArray<LatLng> dataPoints = getSampleData();
    heatMapLayer.setData(dataPoints);

    // the other way to set data
    // note JS array can be set from this method, but only MVCArray from the
    // options setData() method
    // MVCArray<WeightedLocation> weightedDataPoints =
    // MVCArray.newInstance(getSampleWeightedData());
    // heatMapLayer.setDataWeighted(weightedDataPoints);
    GWT.log("Plotting " + dataPoints.length() + " points on HeatMap");
  }

  /**
   * Sample gradient from <a href=
   * "https://google-developers.appspot.com/maps/documentation/javascript/examples/layer-heatmap"
   * >Google Maps Example</a>
   * 
   * @return
   */
  private JsArrayString getSampleGradient() {
    String[] sampleColors = new String[] { "rgba(0, 255, 255, 0)", "rgba(0, 255, 255, 1)", "rgba(0, 191, 255, 1)",
        "rgba(0, 127, 255, 1)", "rgba(0, 63, 255, 1)", "rgba(0, 0, 255, 1)", "rgba(0, 0, 223, 1)",
        "rgba(0, 0, 191, 1)", "rgba(0, 0, 159, 1)", "rgba(0, 0, 127, 1)", "rgba(63, 0, 91, 1)", "rgba(127, 0, 63, 1)",
        "rgba(191, 0, 31, 1)", "rgba(255, 0, 0, 1)" };
    return ArrayHelper.toJsArrayString(sampleColors);
  }

  /**
   * Sample spatial data from <a href=
   * "https://google-developers.appspot.com/maps/documentation/javascript/examples/layer-heatmap"
   * >Google Maps Example</a>
   * 
   * @return
   */
  private JsArray<LatLng> getSampleData() {
    // save a ton of space by encoding as a string
    String encodedData = "eoewFrnbbM{`b@k_EynA~uCrbFjMjlAhyBrLztCdvTmhf@wvJxnYhw[gnXqjn@rgXn_]~pDmxIbgQ{}GorF|p_@r`f@uga@mdo@rg\\u{c@gs_@xd`@idAp`BptM~pLreO{|RtbSflU{kz@en]hec@_cGfgFxxV{yPzqCs_^qxQ`tk@nsKq|LigGswL|yIzdTnbB}iUokFr_@}qDshHvkHt{f@flEgxc@q~Lr`q@qhT_la@nca@uqIebKzq\\vlLtaBegGe`e@koAbse@a~_@yaa@vvc@blBrkBbo\\_pS{iPlm[}gLizXdu_@po]eg_@kuQqaBstEto`@tnOy|c@ahJdmWrkOvqF}~LtsHv~`@epGukb@yhe@suLzaUbeTczS{rIntf@`dB`Dr|H}d@alCueZd^|if@eoVklKfjTuw\\dw@}gAy~Mhsc@~_Lit@{hHu_AlaDt_Lr{Q}|D_qKk~Bt~Cyq`@cpBjkVqse@zhId_\\{_FhoF~hQrI{aObrM||EgeKitl@yoKbfe@~~SiYe{Cqte@omOlmXvcBmkQgKbxUl|Nv_W|x[gew@_ms@haIdhKhkd@~lFkon@{gVtyNrtR|rUon]jBpk@qmVnz^hj@iy@f~LcnPcvQncEyn@tmFvld@g[od\\jhBwlNm_Sfng@|tR|I}cYmkCvzTwiZjhCldFvu@uhBumBprj@qdU{sk@hfXbbYn}GthA{sItaNdbCeyq@qsJvz^b|Cn|Qof\\sxl@veWxwVwd[doAzpc@wxCd{HpxJ_z@llIsxj@{sd@b|d@nzd@}_e@cwLxhc@ipWxfAddj@dx]i{EquS{ha@yvDezHgsM{`BaNjy]heJ}y[gmGubE{aHfgDfmIjdi@m[kgF`z@x~A}mLdzDleUu}Uv~D|wDepAdnH}fKi_`@~[bjCmYy`FycEfa_@htEhzA`rAdqQf_Ja}TicLexZ~cCn{WsdE_nFzwOvtSvkUgsa@{m]duZ_fI{gAr}AyeBh{Bhs@sl]jdDlzNwpd@ka@nqi@vyi@esc@yq_@jiUm|Wc_TztVzzi@|aNk_PcsAf`OjxUccn@iyj@rw`@|mBweIruPriFoza@{bLn_`@_kOcpKbjG~lF{sIyqH``c@n}Dy`\\l[vzm@xpMyzRcjSayb@vsExlIu~BdyUaoVwlE~if@ppCsm]sgXhqTfjc@nmAcwc@uyGxzBr}E{e@k|G{hCwMbj@t_EhpAp{@s{DuaMr_f@vwQgOqjBr~Iqu@qvNifI}oAj`Gl|GhxDuzd@mvArkd@tl[xnHweaAgpk@faXxmL|G|aLqlW}d`@lf\\t_FscF`yY~|Hcy@yrVh_FphUinc@`O|CkYves@fyHadEszCwfGzq@_}i@gnM~af@npJvhJ|b^mcOya[p_Fxk@_sHp~@~oHmpBkpAmh\\mrMbse@t`Lk}j@j{CniZoyk@ydJ|aIpmRbh}@~ey@avZo`t@nzJ}lEyyTkeC~aEuiAjz@qMkfXr~DrcVaea@c~\\nv^cdJ}fQboa@wdC|pSfq`@cqImxHvfC|zIzhFy|k@wcN|e^kk[oiAh`AvmBsfB|wAbN{oBbzYddGnbBwo[ss\\~{XjcAykI`gX`iJrTfoElxG}qFem]ciRkoKdwHd|a@hfPxzAu~CcdDoaQ`zBffLynZ_{@hmXdvBoiJjcJxtH{xIjzBa{@uiObG~jPxaBcdMcsZkmCg\\aqJbrXnuVfOnzF}}E}wNoQopQ|}Ahh^qaDusA}l^n`CvpQ|lDdgPqEr~RecHowOsrH|kJ{eO{|Ltpj@ifa@gaQhr`@ob\\j}CbhHpdExdBpiBbl\\cyHwxCjwC}jT{mIhdMnvHp_HmhJmvg@yt@rlg@dtJmqc@wcMvaDtcN~q_@edMc`b@vtFn`k@jvl@cwM}{i@mnLsjXnnCfzXuvUg`BqoIykHdq_@xqVheBqrHjjEu`GuhF_nWiac@drKvz{@~xUguQo{AofYuhD~`Sgg]i{[x`Sxo]yyK{t@aRtoCdaUy|^lo@d}In_Cvg`@viZejFohVumBkuK|iOj`Jeol@csQelDx{IgVlaBt|a@paMqq@gbb@wyJzfGpvPbgP}vFtaCpsIbxTkwf@wda@~tZvtDx`Jlb@s_Cbe@zD}{E}hg@qlD~lh@btLm{a@}tDjxm@oe_@_ys@tlXdbBbxArd`@bnEemg@e`Ptte@~zMfaCfaDa|C_bg@h|IdrkAdfAgdh@qnFvsBiS{wIsw@ezAhx@_xAypc@n`B_bBajEdyF`jL`t@paCtdZmcDr_Pb|Ck}r@meRdjd@vaLedHlpEdzLa~Ge|c@ttB|_KpaBflEkl\\dpJr}a@wn\\igCo}B_`B~_XptIbhE{oh@iuKj}b@mhOa|Pf__@h{SsbBs[gae@_yQlyh@jdSqsUpn@ahFu}G`zWe_YluRg{EopQzdb@ffAkg`@y_Wje\\j_ZutVuoDiMviBt{\\nkGdzXa}Yen[`nTik@cqc@~n@vvb@ojIytEm}L{kV~eYzjVmyY{aCleb@`eHeuQwlB|cO|fGsaEiiIcKmyQldAeoCwiH|WhxHji^}`h@u|B~aVrsJbiJ}uD`vHowFs_@pw@yaI_~U~~CnsLx_Ax{EycFsrHrpO|mC{sAhtH}uH}}OkhHfgUp|QwaKseFxyJdnAigd@gnDfnJd[`ze@jlDahW{|Fmi@o{UftMxnYohEscBicBnxJm_QocIamElc@j`Qoya@c~SvrYty\\o{BsxPjeT{^oz\\~jZnzo@ymMo_b@d`KzfQyrGgbH{ki@kjRdvz@~_X}rMshBcjg@e}NkaBwaBhjY_kEznNcuEo{_@vfTzh\\xZl{TvrIosl@{mC_~KwkNtcj@l{Bqs@thGpzLuuWkEn`Zsiu@wlRlug@bnJfaM{~[{dPj{`@ijP`nKboNo{j@kzBge@xe@qHqgYdbc@hbk@th@}yi@euEctGijJ|vLfaQlqXk`I{nJ{i`@v~Idhk@gua@cgJbub@ixPebZd_Wjp[|xDux@ysDyA{kBwmXviHjvKo|HuqR|~Bqc@y_Jlni@xpMg}He`Fyw[g^rtWvbFmf[avCziTwuBa_W~v@rzg@prGgr@a{Cmi@yjDeo@yNal`@fkA|~Muy_@nsU|tj@ogd@}{Il_]_~Sg_Ivzd@rwHyiFo~ZcjLfuVf}R~lMioE??_rAdt@r}By`GjrG}bYihLxkZetZp{F`tUofQajTf{Kg}MqqFvab@jRsnEjdN`~Fkui@xqArqc@srKwlEx|BhvMfhJmiEigb@ixBnxb@uyBco^jrDowKe`TfdEbeKx`^esEybJnwUijGiiAvtYohQ~i@zxKgbm@{bM|lv@hnNmfOy__@w{Dt}^niNw_F`_H`vFqz|@aiSdpz@byOccMqsN{`B~z@`jPm|V{um@dxVdkAfsIiNjvBv|`@w_@uc\\_WehGctKsh@djK`u^nl@pvCu`BhSkd^ay^d}a@hxOnhHbzHcgBkfWagJhcWzqF_uCtbFqz^ofZvbd@x~AwpWnkMi_@b|Afma@_mJolp@guFzwe@xtH|r@rtDu|FvpK~qFa|P{kH|tKzbYudk@alm@|~Whi[r`Jpy@mXf_@_x_@krJf~W{h]qfHpaLnnN`{[_`AclNyiUxjUnb]w_Gyh[ewa@bbVt``@szVuua@`xPneFx}Andi@jfD_hj@yzC~x^rnIib[iaPxyMrsAj}ZenYkuv@hzRxqp@fbVghW}mT~rOr^ylYzuGxkWzgEqLw|Kywd@qs@|l_@icZujQrqj@vbd@a~CqbKovFc~_@x{CwjDwyFjlo@~pIetG{RttMsuZ}uh@tfZ{gDduA|nZude@flKtc_@wrg@lQ~vCguCja]`Kktc@zEgkEyoH`qk@`~_@ywj@y{a@|a_@fk_@|fRswQzRhvb@uil@_k_@rtBiiDskKwbKtz@`~LhmI`zFvu@_iBu|AuwC~nb@tfKeqDafh@jiSj{m@wsNsiFidAsmEcqBemKo}V~gKh`TcsOc|Jl_XfzR}pGefOkqFnrFdoLg{XgiE`w@`mEupGcnEdbu@`jK_oUi~c@ypOtnb@lhUwgHk}ApsAi{ZaiBzea@je@yv@ey@qNqhXm}Zho]_hDmfFxip@hpMepMgcQg`Cke^wxJje_@`fSt}F}rM|_J|{WkNoeMonGf{B~vHiqd@qzP{kCm{@phZdyPchNk{Bu~DahB__AgiHhl]fxBupc@|a@lzj@lmK{mDarFyw[_qFvxXttCryDlfDaj]m~Blo\\a`Buk_@giF`kL~~Gs}Mg`A`d[_|AoySboEbdUaHyhc@aeRfp^kyHgkFrde@hwPmzRwhEijWi_e@rkV|^gmFj{OpfRddKwwg@~dFz~a@bqLrjh@kwVuym@o`\\y`D|o`@zjXpkWsvNyfj@dLjt\\_x@mkX|hG`|EvoGhe^eql@qep@f}a@tnEzfA`uSvuHgoEqr@th[gmk@gwPzGcaBbve@b`A}aBmgDeNtrDg{Buub@gvKdbd@bcPgpAoiAo]sla@vjF`bd@u}Cw`]hdRtn\\o{PojRydUf`TztZmsCwDaIukGizZjkDv{`@oXmeHmgd@gk@njg@zr@ceXf_Fj`[e`IwvUfwG|jNsXr|IpaMmjAsl@_iI{iI_kZuuJdi\\esNynMrjb@jkMkoIl|GrjA_cEomFqrMycDh_Ksf@{tLxoUnwRoem@tv@rrh@{|CejN}Sl|Jgpc@e_Gnv\\eaV|o@`wKy|`@nz@dzJd_JhnYrxHs{MkCu_HwbCx~XoiLo~Y`xJc}Arh@y}C}qAlyg@boE_ke@o_J~qCfxCziXb`Eoi`@wmK`|h@b{Si}j@gwSv[eeFj}DkNjm]ztOcy^g|C~lZtaJp}A_dd@gvQ~|IljBb|AyxLxzOdbF{bAv|_@rf[myM}qXbn@etc@t`Hfuf@}dj@chQhl^ioKqwZhaWfr[exBfy@ubAj}Owa]`iAl{FirDdfZyap@crHx_JthD}}DbkDlwt@flIm||@kdXzja@wcKg|UxkNo|Hpy@dcb@llIw^_pb@bdAb{b@yaLp]o_HqxBp_Viq[fyGtdYyuDyaFkch@u|Aptw@wzK_eOdhCqq\\hiF|wUhyYz_ZrhB{`R}}OgyGpeF`jN}kJ`Kf}Ng_DeuDk|i@omVdxi@dzM}}YnbJv|Tue@dgCuzFobBh}@{dCwq^lsGlrd@wt]wlF_yBg~Hxha@tsNa{h@y_Onkn@zlLakc@ccKvh\\blAsv\\vbEpg_@srEigGmhKjySefDivJkgJsh^be\\prTiyOr~BkgFkhNfpYrgWx}Aawm@weEfjc@p}Qa_d@qtYpjFjsLd_`@tOt}Mu~[aiS}uDwwWtfb@xgLr}LvaWi_IidKirW{hZbnXrre@qCmuf@w}BzuZebA{m]ecHf``@tbTgt@fl@bxFkzKosj@wyI`_GjrIt|DjhD`ms@dqq@yw|@en|@hyc@flH}w@ugAk}JabHqzXx`BhtSy|ZjyXt_pAkhk@cor@rp^bvGroMbnBcq_@ueA{qAa`A~xQkm\\obSvuW`rS`tFhxE_xPk|AbeN~aEhyJgsC_sm@k_MraUs}GjmJ{kIy|Fzm\\`zAqqHkdH|aH_gRooXftYvuh@fdQio@vhOgvDwdLwij@ofg@vaEphYqhGa~P`rn@zuTevd@_cT{fCnwHf|f@xwAihMuob@jdXxoBeoPrsI_fAtqYgtWf]n_^gaHggA_h\\_l@jdOxkM|xk@wcs@ov]txc@gbP`_J||Mulm@u}A~kg@zgE}mc@sdDvf`@lqCmtVrbIuhB{pLmi@~xHxqXyrCrkBl{@eiJxbL`cCtVzcEgbPljFdta@chk@m~i@xx_@x_Kva@ywXndGl}TmrFz`M|mFasH|fErbDunB|_JukE{yRiCovAcy^h{B|vVjiQl}B_iMn`Dekb@lgEnze@suUi`XogMvoP|v[ew_@yeJraq@ge[iu^rsLtoRhs_@vbKs}Houh@fbWndg@{cLp{@__HchPud[kxI~a[lz@nx@pmOikd@y{SlkHznPb`[ui_@scd@nbVzrc@dmFdkL`bS_qo@qyXbmR`}DlvNimC_p_@nrAtoT`n@r|KgmI}`d@nxHd__@oeZ|`DjkRmge@hAf|t@paPait@skIviYpiLj}GgsFi~ExnA|nFm|Lmec@tq@ttUvxFawRctFtn`@zmFgv]eeI|ug@dwBidGrmDejC}bBuzBub\\{iRjj^qyDygJx|b@r|FazAcd_@g|e@fcSvch@x`MzsAvjDqlRklJ~mP}e@ceN~gMqkQupBfu[gcHtuDdnEc~QnbAzfNecZgb`@j_Odxb@h}EtlJbfBq}o@ilExvClsHrz_@czG`wNteNwqT_aLlqOi_`@_oFbos@qcMkrR|aD`~DwcI`hIvxKooM}eBgyAdk@nS~cEydBt~Cjza@{he@_bYt_JgjTb_d@r}_@kzl@}xHowGeiEpki@uuByuH`qGag^{sFdmZliBmhSgWgSanIfc_@xmO{jAozFcmEq`]udAfe^prPloUucL{wT_q\\sMeVwkFru`@cc@o`h@if@zbg@qcBizm@peIxbOahHpz[pcS_}ZkQybFuiJz{d@{hAcwAxiGiu\\epArjAv`Elie@zgj@ugTefa@ejPiu\\lgWidGmiWjz\\a~LshRhdFfxGh}`@vpQcaYc|\\~ab@rpH{iP{s@wxAg~UbbS|a_@yfj@_|Di}DabK`_l@b}P{kCp}FjiLj`Asrl@qjLxqBcxC~mY`dJxuA{iL__\\w`@oI~fCb}_@`yA_jYtkHjySqcE{lZ_pBj}Tc{\\ybG|vl@iHaxNdrQmf@yv]`kJurHktP`xx@xgYmePqqNfmAddEaoi@elBn}c@uu@heL_ea@wvf@lj\\whDayBb`C`sKjf_@k{DgtFx~Co`JgdGovEroH{vJkrCprt@hrMgta@qiY|tR`nRgw]urAz_^dwAqbDkbBwkJ~aGmmOeca@mjKpoH`mK|jNjwYobd@ucMx}p@ztXpdDacSwkVjd[nwOscn@a~`@~kn@rvYwa{@eoYdeKnlNx{J`{QstFi{LkdDwjMzqWu}HonY`vRpj`@kbDkaCd~Qk~ZcaGj`CnhB_`HqxH~rc@hIwvClmCwfYs_GkjGnxAhsW~pQsbReuNfla@wNkac@ylDvac@jjKarEgaCszZa`@bka@{kJp|Iz~e@a{i@alU}u@k_Ov_f@nhDgtJ|tGk}RdKhmZl~BlpNtdAczr@ogFv`BczBl_\\ySbtSrpNugPwzKerg@_|Jfrk@phQozCqrHhzDhsGifNdkDjsEjYlQc|Mnq@vrFszJkoPa~\\ez@h`k@fvGy_RvfRarDo|AsnJ}gP~f`@liJ}v[{{@qx@icBxaRb|Occ`@ov]dcj@pjGwuJ`pSad[enQtyb@`dBa~Th\\dzi@l`J{_p@q}KzwIzwCwnNcsSng^xe]cpUsvH~rA{zAzyXidAtqBneAgaPmkSpc]mqG{~r@`g]r}BbzA{wE_yPrym@jpGerFf]sIwpF__LlkWmeMipVnlZncOou@io_@}n[zzW~h\\ciV{rJzpPd|ImeRgt]n_Rx{Wj|NgoV{nMdaW`lE|hGnOqja@ky@tx[r_B}m\\uoDht]f~AgQivS_uC`dXh|FrrCppGk~CggM{t\\ifYxfXbgMyqPrtC~gKo|NxkDqtG{u@f`a@kxUr}EhvWg{CvtD_bA~aFoxOcbYe}Ito@dwU``JzxYvlE__Snn@sab@qxJx_Kj~IlvWg`Fhc[f~p@ya^a~j@iaV``@pt@sW~cXgkDyaCywVw@d`HloQf_[_fp@yeN~i`@y`LdbAg~F~zEjwOczBdrM~VokAkbFiwBxhDpiE}`UgiZrjPotAdnEt}Ueid@ikAfz\\jhHn}OdcDyaHkxIm}e@uqTjs`@`yU}jDq`Wb~GrhYks^a_A{wCyDzw@efK|~BxjOnrZa``@uvXro^flWcyFn`TdkOiwR_tW_}d@z}Jjtb@__J~eCfiJsxb@y{Bz{DziDdlUqK|dDgsAyg@a{Dpu@biP|uO~nUufn@oua@vgb@~iJylIf{ErvDuyS|zL~mRo{LozKkyUw_JxbQd_FvrPog^mft@`}Kpsh@rpNux[huGokA}Ofq@ekObwOnbZusCu{M|_NclEg{YdeDrqVj{Cwx\\g~Dp__@n_@k}\\y}I|hXfqLx|CisV~~Po]cda@dwCdpAfoE|}Pj|QzzCc`M{c^jpJpbVsra@is]dxUxvq@tkTe}UmqLfiAwoCy_D}q\\ypGvqo@oaYwvZxuo@rhQsnc@c_G|uWoBy{CjiMpfI}uE{dKqrNthT~tOcyNajHyxNc}KttQjcRmeA_jXjzGrjZe_Jab]qva@n{Knih@rlQvqLlnCwo_@cgLrfNdrCgxIaxKbyLnjLvmN_hc@ycm@bha@qs@aTnzO|gIpoNitKzoAcnBsrCvcIgnMgnAykO_oCwpFurHdmk@h`Gem^`Rgp@~wAvoLyxKrpa@bd[mh]o}YxcMqgQm{@zh^m|e@avOhfc@obK_m]n_Tp{b@jd@qsd@|Ddn_@|qIz_Lp{Ng{Iow\\utOn|Te{FaeJun@`R~{Qcd_@peFbfCqi[vfYlja@kbHsc`@|pMrn[yxMkgOekLjkQj|[k_b@k}Hz_]mgGq_O~]pzR|fJcuUiyBykCreDo~CpsA`fQk_@llKaqXyQ{vHahd@rhXtkb@`qD|gCy}Dsf^tyHjtJmhThsMeyEaTrmJezZjwNr_QkqHnrGivGpxAylPknVrqe@knAotBtcMuhHsuVrlCv~Lx|EqaBjhBzmTca`@`_Nr{[y`CbhEutp@{gT~sNlsPolLy}Pdxh@fbHd{DfaIssKuhEmiBsiOhpJrnOvfAnK}mEpqDqrb@qmP`uTdwSfnE_jXc{U|zBnno@}tMwoNd{^{nAypd@peAds_@a`FcnX~iDpx[ow\\{|IvoHwvTtmPrdf@gdD`GvmJsxKohEbtBka\\egHpv^qpHnmAfvWv~HxKilJg~PrBdjLqrLzlHtk`@~_Yy}x@isw@jra@juSz~HhoG`[sjOofg@n}Atb^nbFpqFvs@icDh@{_^cy@ayE}eD~kC_tB|v_@ah_@{{c@|c^brb@~rAw_b@gfAh`f@}xBmoCc{Qkl`@xrTpdCpsHcB{jCxmCxcDgpCmaMty]cvHnnEv~ZnjHkhe@a~Vjph@p}C{kGmoV|n@nz[uaCgjB~|Jg`]iyOdoMhjL|aJszGghF~eNmeJq_J|UuFrpD}jCdoLd}CarZylQjjCyrExzVzmFml_@vpJpaRdnL|lMw~Mwc]d\\jb^ynP|^x_WtvAhOymCov@acI`tIhmKyeIxcDrPw{V{gC|xa@ngA{jr@eqGni\\etTvtItwj@gy`@ytXguBlmDjgR|\\`cH_fNsgW`wQ_wEelE~qc@pqDinBdgDhgBy~MjbJ~wg@azNegb@huEf`Ck`BuoIaeNsNjnSpzNqbSulVwaWddCliYdj@_yWzb@hcB|zFb_Bt_Az{^wqT{lL~gOauNlq@jtS~cPi`PmkKu|EkMvNy|Gv|VcuUotPzpYlmX}j@eed@anBi{Bem@pag@siJb`Ih{aAufOkq`AioYxoS`kAsnG|aPnnZqaNoqYfvFh|LxyR{ya@}cAnuB}{@p_Dn`Xd{c@guBqvJ{nOu`\\ubTz}^eqJ{yFbdHzmFzZubAooLcxGzdl@|mIwtj@krGilC}vLmMluHhcJtkFgmHwxAbsf@_pFmiLhxHhkKzuCkua@smI|lNd{JvwQoaDhhDfuF}kUqzLn~YndPm{f@qcFi\\iiPxin@_vLivHbdl@urHmaS{q\\km@~yq@fnCchh@dg@b}MtkJoiSchZh{_@nwRi~A_gQgtg@xlF`ea@vyB_cKptMplc@ya_@{gy@~}BpzA`}Bpaf@|}OisDovi@mUdhb@`xKzmFepF_`ColLe|VzlR`sX}~e@{{MlbYsaKrh@hkKxlEsqUy}Klyl@hoLaoMyxYj{GbpQifh@~_Enf@v_Ero_A}MizRw~j@}uNxrf@gvFqeGimPhuJ`xT{k@}E{iDptFgdXqfEdab@cH{lIjtBcgJvfQlpIc{OkuZ_yDjql@ptIyoZ}aKdbMuiBm{XlbP~{W}pJkl]zvFngZnvC}r_@{sJ~vh@lgOmfCi{GymBnyBgpBcJ`qFk_AdoAbqLwxK_}AaiVmoI`k@pu@~kNopKv~NxdMim\\oBhd]wwLPjsB`Mc_E}k@l_VkvMw{LdhT|`Gmw[ml@fy]~uWcmYkxYfgLqsDa|R</a|kMv_GddGsgJtsNdqBo|QaqDjyUxnNiWpm@k`\\eaCirNutKf~HjhEjrUy`@ck@eaDrXqaDezWrdFfmS{cBiy@k~Dgx[zsJenFmxAniI~pBy{IcnHblOykI{}Dhi@aeCngKtzXei[jdF|kV_bDs{Reea@xrTreThiE{xD}}OucQzpCnzDx|Lm{DatJ`w]skUrsD`dd@beAmyClzAgdJimPpf@bhHm}@nhC|vM{lEcaa@g~Uprb@~dWfgGahYwgPliQzzPh{Lur@lsGizFuhb@yhB`xVbtBpmV`cCazT}eJmqa@~fAlzG}_FigH|m@rlf@jg@}oBjwFo~f@}}C|t`@hcCfoBif[an\\fjUnqK~qFjkWfnBen[irAhuAgmVheHfjIdmKofPijEjp[dgEid]}}`@rdTzq[|~Hq{PiWr}T{yXteA}g@wUz|Cem\\peM}cKwgFhmr@|kM{[liQyxG}iMqs@geDlaCclFou\\xdAznj@l`l@yvKcah@zgAltAsvAupLjwAfcPqel@{}Hrv^`tO{cQaqOgwBfnC|dp@luG{wl@gsHs|Euh@dn^huGntGgg@qlH}sAtzBq{b@wgWxcd@{lPetOdxo@`gj@kuI}x_@jrEol@mw\\~gEokJqfHhtF`oIzbZ_wA|`Kpcg@}vFsab@_mb@k_Dn_j@wgAwxDruDpGu`Mu|N~zBcjTtSoEm@xqp@asUovNzjj@hnMopp@gjLl|^vqBe|Cowj@alBx}f@~fFd_@gwEh_AvbOwq]weBcyA_`JvuZtqKp~AsX{uKs{Ond\\xbn@qlj@}lb@j}h@jjJ__n@ogRjab@xuNphBv{`@weHcbk@kEu}PyyAfgUe`TrcCtqXmgNruBzfFpChBsvDhsEsvc@}fNro^bxPfnJxnJyCs~Tyn@mw^cdSdt^~|TnvHyy`@_c@kfEslI|yYjoRkn[gzW`i@h_KpsAga@lfd@_pBavZd|Fk_CemCr~YgvUgb@nfHbtBbkYzaBw_Eidg@wcMpea@_qNkuSbwVf~RoeRz}Nju_@gyLeoQrGi{A|{@|tKlA_fD_fNxbKr~@izZjp[vaPudr@wbFjg_@ztEyo`@rFzvFhhD`Ck~GqoEdeDkbBuyG|w_@ys]vpFb{j@enFixBj{J{oErqKrdf@w|q@el^hb]`rJys@iuXnr`@jz}@_~f@ajf@caWmmN`ga@twA}`YnjBmeDixJpu^jpFwaB{jReeBmeFpuFpjQkzAcfVntGd{jAhcHi~G{}KwnY}o_@e`H?|nE`|k@bVk}n@auQ_nAb}G~vr@vaHmoRk~@ao[i`IltZdcIsx@ahF{}[hdBxpf@tmi@m}Ca{i@g~f@yxBfeB`g@tiStaAhzKw_BzwNo|SwpJ`ye@akFycc@_gO`jXyoSayTz_DbiLvvXorH{{P~tHw|Gko@plDf}BtVbhCbh`@g_B_}Hd`FwlEetGxoEkh[lq@vma@arMvf@ndRyd@mfVjoCzz[wu@cuDf{H{{_@ybSnwIzhFgTsz@r{UlwCq@ihB}__@mcKjiDhuJl~h@|{CoiJ}wAgo`@msKvul@ybU}pr@xvVfs_@fsA`|Eak@apd@qyL`q]ryXazc@}yXzwh@h~WkbJ}ee@rtIfxZykD~tE{ac@alJdai@y{@jqFzvQ_ya@cwD~qTb|@unMx`HhmTy{L_kBxpC{_@|Pkwa@cjFgFbnAlx@osBzoFgqFpoLv}Edc@_`Fu}OoyE~eXkbH}zg@dcVttLjwBryF~lAxyIrvKfeEm_c@{}Dn}b@qpNklNjf[pkBrUh`B|kCeoUymb@n{K|oFtjLacN}dNb{b@brJm{Cu|@eoK{y@`_NykG}iThzIdvHhgB~kPcnIknAhIcxOzDtwXpdBum@_~D_l\\~_Hm~PolQprU_a@xlXf~^qxb@kkIdcYsm@fwR|z@cfx@kyJ~lAeaF`vWzlGrrJbnNwcW{fJw`GueRhgD|wPfxTdzFe|R{tBbxUqrTzzKrlQm`b@doEnt[igDsni@wgFl`z@njPicl@gbEja@ggAdbK|kL~~LwrR{ca@vvDb`e@zyCerLlxBgs^scR|hVqtMtiZr}i@mjq@ax]zJ{nDbjHzaTxLifAzz\\wyD_fC{jShbAjoUoxH|lEs_PguIp}WrbA}eb@fC~o`@ijJycVhzOj|g@ugd@mfPzuDcdK~sFieHxvUfwDi_b@ztPpjI~pNtmu@usm@mja@xz`@fcA_~Yk~AleYh}E`T{gc@_{Bjek@coYs{Urx]xzMiw^e}Bh{a@q|BkPhzHwnDpPsj^ydE|rd@y_JaaA`rLljIu}AqyQmd\\r|D~jd@m_c@yvIjuMxpFwdNeeIvvk@|qs@wze@uqn@fzZxtGa`_@ofNve`@dyL|bHgxCojc@crI~}Pk~QnsJjh[uXpgDuyNfqBa}KwmBhi\\gpYwh[plWofK{wJvpNh|Mz{SccE|s@__ZzeFjha@yoEiqCyuTxfA|n\\udDyz@crEct`@wsCz`YxoLxiDohIysc@mqAxgc@viPyf\\apIvq_@wk@{qh@u_KnqEpfTb{Dzj@zj@i`@~Mwf@ytA@llZyiH_}@qpAbn@hhIqya@wpP`iq@zk~@}bL{dg@n}Gmtf@o|DjmoA~Digi@a`c@alHacD~aAruExmEf_Toeb@voJhe}@}sa@ihe@t~]vaGveHvp[}{Pa`z@tpFteYohNb~DuhKcfBwvEu}Blfa@l}KjsJz`Dmti@ctIuqCwtAvnYqac@f`Ijai@y\\y{g@}f\\xs]wxGgZnvItiH~i@oMckLiyNxjh@xkO``BwfLm~]rrIlaZkhFq`W}~NajKdaV`EenDdrg@{{@cfa@_F~kEe|IrvGn|_@t}g@tvk@agy@as_Avy_@ePjDfjIq`a@mnPxze@lbScwh@_}OzxAfaEsyD~r@vwGloAfK~GfcXkkWkqH~ob@shNk_Lr|Fm~Xfbb@fk\\se\\n`K_sJotLg_FwfEbqZejWljWjhi@cjn@olNd`E||If_[gsPvVpaYgxBubNogf@_iBldd@doBoaApbC_jYylKto[`hDmsAxk@{gc@qjG`zLngKwaGmkPeiBzuD`_g@bpGozn@a`O~{[|hQgzFib@hgMjvH}}SwfNjc@o~CcIr{KeLylXll@l`Vs{AisFywEaj@hrj@`bK_gEwoPus[lyOre[a}GlzOkcWg|^|fXasEr{DucHk{Pj`d@n_Jmud@vpB~hk@kya@waKxmc@|bDlmCsc_@kcErea@wu@uyTgeKxgFhbP}}T__I{xK_{Ktwm@`mQqsCd_CayAcy[vhDprNyvd@r`Gdx^fkEauX}h@`|ZwxLyhDidW_|Xfx^td@`i@pgFpwBdsTdeCm`LhnChrGuwb@_hTjrRf_El|HcqQcmP|gHb{E~iX{_]p}Dhac@nfFx}g@z`Ga|d@u{\\ewKgf@qn@opM~|FjmVivVyiQjv]bxSds@{kg@omTt|InrKfw\\laL|~E_sHoq\\xzBv_Yub_@kcKjrNydOte@tM|`NrbGg`\\n`VtvWkkDn]|@czPztAppUgbAmnDsiXwnCntm@x_Pg_OubT~aEryv@mjUam^zeKmvPfmAkjFmqExyIl~Den@anf@w~Eh~e@igA_yAprOm}b@mlJv~f@boBip_@|p@qkAkpH|sIf_Lrye@gi^sfh@nw_@p}T{`\\x~Qr|c@qtSevHeoJaiKrhRnlC{od@fsDdxb@acD}cGxKz`GhaBw~Sl}JolMihP`ic@vxAymc@sPn~Fle@jcZd`FioBel]ix[`zVnkFhoBjxTc^`cCcvFsuc@fS|{DlkA|ib@nvHq~Bw}Eq~@lPwub@ao@ntV|mNir]ew^duE~uJllj@cPkzg@raIreAopBwdF}xCmLjf@vgOqwRcvArk^goBawCdzc@vo^wxAarBsqGuqw@ljC`vVa|^vbE~e]tRs{BpYhkGh@dsChtf@ijr@ga{@zqj@rbPayNggKy}SzvDfo`@esBeia@vfCveGhpBxu@jkCnbScig@puFdm]{ue@~uCvch@`gGkzBu^~nBggIseHrnTwf\\caVlla@``HeaB|d@c}]ac@jp^bMg`S{qVpuYt_YmtIi~^lhLnf]{jFayBmk]qiD|`h@`aFk~n@isHn_CpcBeaFiwJvuGt}CppCs|Cbk_@l~TnaDvaVzxHusOqjt@krN|pn@{i]gqj@|zd@vlA_Bnw^giEjsJjjI_|m@}vDh]y|OnqLr{GlnUhbRyqh@urQfg`@}yD~yEdtIwbNhzK_z[yzWxmCzlDl_g@x{EgfG`xBjlDemMz]p|@cdAxhEwfQ_zSziWx|eA}sHmsg@on[gyBx_A}bDjiVnfHhvV_sGuxGzl@}_YtfLz`QtCwlc@_aS~zDxtGb~DrbDdtPy{Fo{@x}KliEsiN}q]poAj_^|fBvhQxzGmz{@{fRfcN`cLwlDcfBfpCi_Gq[kj@zq]`xCc|b@|{Alie@v}@kpe@{nGtqFbvG`p[a}F_pB{kIeq@_vTe}CntLhvD|q_@ptIpgUkjVuwQtu\\uiHkbf@}@isIgoBzfh@qrPln@`_WgiQbkH{zQyfVf~\\jzNaf_@imEdy]atPgcL|mRs{VsbR|w`@ftNyjJvjK~aWvqPnkF}tLssk@unL~d[rxF_qe@w|Nryc@}kNrkCnhF`Eb`Pd~CehGi`]pgG{aDzg@`|^ydFyYj{A_y^pbCktGyrNrqCnmDn`AtvCzje@nsMk|_@u`Nbh^`mFl@qoAofc@}OgiBg~Jfgq@`rU_yo@etQhuAaaOaDrlW|aZgnR{y\\f}Mjyo@abXeaPz~]huEteIo}_@omb@bnUx~U_nWehGh~[l}Ftm@xhAglb@sdT|y@vsFbnb@gkT{zXxt^vfZoToxXt|@e{Hk|NvrQidCjhPfzPgxDziApdIpgLssUyxYclUfdI~uWxaLbnJeaN{i@ltFk_AzsCjnFg}Oy{A~cF{xA``BzjCw~CqnBfe@u}c@qmLnso@~rMvhEoqVc_m@jhLrh^rfBreIh}B}{KzgFqq[ovGria@hdCpdIwx_@_bp@j|Y|yh@xef@y~Uytk@kfKlhIhxZjZnvN|e[{pRq`[an[qV`eOarPrfQtgOaf`@shJwyDl|Hlnq@`j@_gi@_lBdfb@dfFm`d@y}E_`Dve@dh[inT|sAmdI{da@hd\\tiBuc@jgHzlLncQoyEuvJbcBhdT{aBmiYl_A}eMwfNomCdDvmI~wAlb]dfNhk@o~@bhFtgB{bUm|NilOthIv{_@qfJibb@hoEpkEzrI`g\\n]zl@qxCtb@g|I}|PhfW~~R}fM}aWa}DteYdxC}eWrjAjcUorFe`Cs~Aeu^tyDlkz@tq{@{vu@y}z@kfFdd@fsc@gVo{A`b@qgN_vFqiWibFxxe@fpM}gd@y|Ct_JloCbzUg~YleD|cTgs`@_xDlxe@nnt@rlAwjJy_c@i{YbyZkpIhvJcvV_gOzw]q|JisQjoTnnKkgl@}|@dtd@_wVytb@~lTfgMv{MnuWegGs}Gl`GlmHyoGll@trIgm[mfA~wW|gCepB}fGo_GeqI~bLv`Pcic@axO|sYsnVr{L~f[uvGs~QevOh{]vg[wxE{rRp~N|mBsck@feElwTsg[fvJmjAchF|Az{EisEahIn{f@eTu@ca@lkJvjJuqGg_IocHkvNo|YfiRihAdiDvvOex[blPbkWtyB~|FqiSs`PaaQ|sEfg\\uoOotUx`Ypv[qcJwj@gmNkuMz{`@v`LcaG`xCogDchg@a|@zjIj{Er_c@~{P}|Gcum@sf`@jeUz_DjaAff@zKqjDlzAvoGyb@}cFmdFtjNsYn`O`MuPpnJm~`@spElc]krb@l{Ax~g@wh@upBooJqaGz~Gn|Io`GevO`pPpiQtr@t]kxFguFxrG~gEkaIpDsuUivDtS|pE`of@|cIa`HioTspl@ojBbln@j~Muxa@abHf{\\j|EctCvhBg_Cyd`@h}@fk[lwEap@y}AbAulY|pDqwKqtP|xHrWj}Zg|@wli@ql@rlk@naQ_i[uxCkhCy}Fr|^ljH~cAkwDzjFx`H{dLsqD|oB_wGsdb@zgFn}TzvL~zKi|K}~Yxz@|~J}kRvj[p{q@ikHw_f@m\\npGgoBpcF_sCej_@tzE`z\\{qc@s|Hzia@lwKek\\ifCv}`@ucb@orZ|i`@jbUghAhjGk_C_uC|aEy`A|Qmcb@cbGjrd@zcFuec@}gC|bl@rg`@ytGmq`@enBlcOnFymN_FllBo_e@quK~jd@zpMhsCaeBagI{oCg_a@v_@~oQzxFw_HumEaxCg|HvtFpuJb_`@aq@yPk`CfhLr~Rsio@yqQf{HjbGq}EpeA~sVcsEdyEagClmJyxZwhk@lq^dtCvh@pxS}FvgFcaUcnBpoXgm[slLrw_@_`RuxB~ub@s[}pe@gkFrtYkvDdiDrqJd`@ufBn|HvrAd_@mxS_oFnz@v\\tlRqm`@wrVhy^spHwsGbtd@b_Cku]jvC~`Y~On{G_sA}bd@yn@xqn@dkEypg@w~D{{GceD`oe@ppHcwFrdEqnZepFbtPsaKs{ZgmH~zp@lnNiwKf|FgcCrzFe}SaeTf`_@jbMepD}bH`sFriGaLo{DmxAyzFseb@~}Ap|@{gJlrr@h}]srUo`BhiCohM}rd@`Dxwc@ljIewJe{[xqJn`U_sAjvCqva@iuJnmt@`lRajHmaEqeb@a`GrwC|jCjjb@|rQvqBupr@ikRzbh@doKm|E_dEkwFgnl@yaJtl@njKtxQj~DvbW{sEgu_@rAtsXi_XmcPrhe@qu@ooA~eUutAgkHawZ`nLvjUr}LclW_lHlh^uuFyOiwCk{It|C{uBwn`@luEydE_eMruf@vwS_qYg|Njwl@nsQmas@sxZl~AzeRxz`@s]oeLbfOglV{iQlv_@cl]gUtin@f}Gf`]e}m@_~x@~{g@bzMiyi@s{PbjId_Nd}c@ncb@}mb@_p\\`ue@txd@gaHm~n@c{Yv|GrhMdsJ`mManTc~AfaGl_DpoFqyf@atKlsE~fHzoD{d@axK_iE}Fu~N|JjO}fD`tEliFbsJxlc@rDwaQxnL~ab@fhAisPiaN}wCwx]opLvg^b`GlnMpcGweMmoLssH`lQlnOksG}qI`cC`|Bre@`cAauDgiCtlAxgHfjBuqEgaZu^fxa@dtNkcb@ecKje_@z{MylHanGlsBymEuzZu~EdtBxqElqXieLsxi@seG~`f@d}]o|Am_k@nnJjbUehf@ycA|}a@vxFa_g@krJvsf@xpCiR`fPobAejHm]gkT_}A`tYkuTtp@bch@okd@y{k@daYje]hfWyl_@uwXvy`@~eDqQgmFa{e@pt@xgd@`uN{a`@}gKxeAjeCmD}eGnq]~gFw_E~qOkdNotHlySgsD_z[dt@tsRvuMfxBm`SjaOzfAsii@pfAzba@ox@gxB`}A~qDouF}g]p|KvhWl_CgsHge\\ckOlpMohFw{@|~Cz}ClhGduBmeA|LjgZ}~Kny@jhCsab@|bCpkb@rnFslMqqR{}P|`FopDimJlpd@vaNokAwpGkbOc_G`yJyrO~tH|th@gvX~yAorG}aMhmWlpIxc@oo@xiH|X}eSruGsrKoeHblw@fks@gr[eajAma]nbMxbYu`QlfGx`g@cod@a~]rvAvqLjuWbfDc{\\aqFj|\\vgEs`d@w}Pn}NflTngQ_h@a~IxiBzdVum@qsBxdD_|Fygm@qr\\xy]hzb@aaRgt[x_XpoX~zJbrAuqLfsAxtDoy`@suGb_DzkDwwFoiI`rOloN`bDdvCraYixEcym@epFrdA{iGeNprFtp]d`AnuCkgAs}]qcDrhYqyV|Yjc\\|k@|yAt|Sdde@{zn@ui_@dzb@veLu|Yakj@w{IpnQra[ubFw|a@|~Dj`P}qTl{Nxe]wjFyuc@bwCdpB}l_@j{VhfHlwAnfWh~Dksd@cjMrpU|~J~wPaIpx@jwEuePuvRot\\l_GdqAqgGxfp@dy\\`rHnoKa_Nsbb@`_DxvI_hf@aiCdfD}rEjjTmfRkzNz`\\dmRkiDggPrnF~vRfk@spd@e}Tvbf@xzC{bRs|XgoFxfi@lxc@t`Molh@}pTpxj@d`IajOwhIj_Iz_Ao{h@{oEzzBjvCvn_@wlI_eQr|Wro_@poPg{{@g{`@hbm@tsGudb@w|KpnFe_Fw{ClpDzf]d~ToeCqyHqcC}}VhhIntSqob@k}B|CkoFdpj@lvN{dMylYqg`@rqR`bj@bAst_@sdPhsDzgXbqSu}@ehUqi@wxC~_@g~EudGtde@||AtH_wDw|AtlCy`d@sxIfrK|rRjxg@fsTcjm@ub\\nq\\fj@v|Cp~Fv|Ksq`@als@xsTfme@et^neDdhoA_^ga`@ieBkvIes@blEcc^wbIjy`@l|Ioud@ogN|bb@xuCamDtpC_eZixAz_o@}vQ}cSkiAb~BjmVroFhwAki@~df@{sFe|f@xu@k_MxV~iQu~QvrCxbL}mBknUq`Izal@dpOyGgXccn@{rZj~c@bzKwbDym@bwCu~@y~DbyFytc@suEvsBodJscAp}Kft^du@e{[soFb{e@lsSsc[_uR_rHbxGxsTxfIurToxNdmZv{CbhCz|Aw{G_vU`zC~mWiaEw~LbmH|ZneEdye@wdc@y|t@hin@|l_@kmUmsTwiHx`BtcGgcMnxGp|f@gdb@}_XhzYplFiv]c`Enwa@s^uxDbyHcob@{}PbvXfaCfaVtux@ohb@wjq@l`e@d{B|qEzmE~}@`xFq{KvOkmBkmIxsEf|d@ikm@wgp@lvb@wlIw{LheLagLwqAhc[jnK_nBq~^yd\\xmQtaAhIvpYhkJoiHilK`jL~iGzfGa~BanMnvR{jQ{dLmtDwRpc@{sU}oC~_SuGyxCyeA_|Dim@pxCfS}Mbqn@jpSo~@seLscAvLshBjkMmbA}gO`NpCzWgrAgQlWcFw[vMor@w[p{E}qCeBjf@y_A_cA~]cvBpxBhlWyp`@umRvoIoPrbFdZgmFfm@p~Ev@xKg|DmiTziEpyIqbEfzEomJbjL~oBw{H|wk@lxz@_aOqo@ojLsph@cxa@qxKbfp@ddU_xc@whJjr\\zoZucFitf@waA`}Iwa^jMvxTorLkqJ`cSrwRqvWqT_{SaxXjpb@`|e@dsDaqo@y`MfiImfB~sRzoPn|@`qFt{Huk@}cKu{FjgF|mKmZsaK|kBysLnaA|eAm|`@zhF~oHyfYajHpuMeaI{k@~zQlcQooKqfE`bl@bz_@y}ImbZzjAklAmb_@cwD~~]pdIeTisBz}QrlCowVsdFzcF{~Dd_PjwT}qSiLiHevVcRjeIfzT|eLa`QedLtn@a]jjKwsBsor@}tA`tGjfH~d[wiOmif@`iHuv@mrJxbq@twu@ehGmig@_lGqyWldJhrZoiPsyNnvJ{fQfcSljf@eoIsaGkrGf{D_b]oqNdvAxyEhx\\xDhfPpxNyjo@gfGbyKejLlyVfgNy{HkcE|~NpcLebNs}TuDzoTobd@ct[f{x@ze[en_@ueKsxKsw@dxVb`B`_Kpo\\swf@em^roWsdZtaKv|z@ip`@{u^~oXu~F_lYvpIzs\\ida@i|b@j|[tig@ucAyl_@~mAxtj@hkFe|Siw[yhPjrEkc@j{TvwOsqE`wQ~hMkjKidWeg]hqMwhFewNbtJbaSfdf@{jg@{rm@bwTbg[wdBel`@eoA`jg@ttLm__@acBruCboLyrAi`@ezGgiJ{lDclIjbDbuJetAa|A~ol@nbKskB_rE{J}{@w{AykFgmAbK{kBfrIxTul[|mHakGot@of@o{E~iAl`Ds{@qzOjpMjeLswDo[crC`i@df@ycBe_Au{Ktdb@jsZzd^uu@ewOsfIule@{kY|{Orv`@~oCosW|fKd~L_{^ejU|hPpsJihPgkWlmGb|WlaXsxSggLzq_@oe@t{C_^}ue@_rB|eFtmHdfi@`gKe|GqfJ_aEorQshVnvR|r[ccIcbAr|Icy@}nV_fQfb\\~bZpq@_jd@mgSldo@nfK{|BvfKmeQwcMnv@udCfaBajW}`BopAsrBrtW~oCtlElrF~`AmmO_wRcvRndMkg@fLjlD`uB|_FwcIodDhrHytCyXf{Aw`BlgY|mDy`EuO_hEl~DqrZymVp|a@c}L_ma@prJzag@_hNffTd~pAagMcet@wkCpxGyq@k{Qs}@xgH{l[w`@h`[paEntGcqDolLhdEriG{zGlbA`yBqbEquT}nNtySpnIjbS~oKagJr~HpBwaj@ez@jnAc}Adc_@hzMvq@_fQimBx|EypD|vAzbFazHk~m@eoJn|g@{lPmeg@b{Vd~c@dwG_rBdcAt|B{vIvoT|eTkq}@sa[xpn@`zM}iDwdW{bCboZwrKflA_kEksB}bDa~BikBu\\tzBozKp|n@jc\\cym@{nM{yCglJvjPrpYweWasXbwB|rBo`AfR~}C}~Fdg\\jpRn{Hlej@_ef@{bv@rsB}|F`nVelMvuU`q]ijOipIz|H|uWcnq@g{\\rfPjlQnnDj`I~qSipFokBurB|wM{e]mfNx}OyeE`oXgyCucC|pKchDuh[u{G}nPcaIbr{@_vLmk_@ziRb}\\wwXi_@jhnA_hP_kg@ghZcuHttc@tsRmbNkgAvcC}fIozOrq@p_Smte@xg@`a^cc_@fPxn^a|EdjA|_BhxA`jBcy@myL{tArtP{vEipb@hqB`pe@zmP~di@_cm@{is@z~M{cBdDjaSnpUbdDakg@cpRd|a@nkCudYo{@~_b@ddTsnBssPo~\\fMfdZ}aIqu]``EfjZx_LeiL|}@c}PoiNj}a@zSs|Bm~H_hYjuHj}Ugt_@v`@ih@ola@ppSdzRx`T`hYz{Iofp@mjb@z}u@mtN}kRhfp@ymWc{RbsNqp@`nNh~D{ZudUvgC|l\\y`C}Bxu@ayOjeB``Jwba@{fG|y_@~|C||AnzAmiIfbGcePkrJhuMwfWfmFxgTuqCnbGg{d@kcNrid@`kMdhIkeEc~CnrEmsMqsByyIdtDb{ZknFs|d@bbD`dV__^zjJpqRh{D`cTugj@qoU|i]fxN~oGscGbfDvrr@iwHizgA`|BxoUjqC|fa@aqHumYii`@{xOrb@cNvyAxmC|ro@n_b@wnVsiMeqHktAvrAmeWiuSb}Gln@wu@v_`@{h[trDlra@gbEshAcbZlqAnnYlhI}z_@swN`iGfdHoIghHyrLieGnsq@`i{@amc@iyj@fpY~gK{oAmzGgfh@_uSxgz@mnPwrR`m]bjAbwFz~AmeIw~NpmGdeJqkB_vZoxFtsHfnOj~O}|OdmDt`Ik|[~QfgR|`F|vBkvm@ylXrtT~t`@b{[os]ojY~dKbcN_{NmpOjy_@frFd_BfgK`c@yCgia@}bIvz@r|E~qSghAy_WaiDfoMkzGtxIpbXtgDq{R}cBttFgsCizR}qR~rYftSqWkfVkxI~sKt{F{tDxj@caHwxEoaFi^|~b@gmU_gb@|aNjf`@ddOc@mkWibK`f_@`bTytFsoBuyMuePftTyvOwkIzcFxUb~^j{@wcUy}U}`Bt`YahTekObrH`bHt`^hdOc{O_uE}_LawEkGwcG~nE`vFn{XmlAitArMh~RnvLksX{mo@nxXn}m@m`SoqD_eA{kIlxDo\\uuUztD}aXqaOtnl@rsOs`GdmIonR{zL~vUwhXnxCtlYgbFkd`@v_D`wc@a}d@uyFvn[c`a@~fQn|mA_}c@wlk@mvAf}EqyE}sEfya@nkLm`W}fHdkWpe@nwHcJi_Nv~GbxN~pDsmFwwUcxg@}iElqc@sdHjwDz{Sweh@_cMfdc@neY|uRwql@ifNhyZ_ce@}bHr_I`uThjs@x{n@}y|@ax{@xcb@zcJieYyoKqpAhr@jsg@||MukOq`j@qoAdkn@rbNpjCetTkxJjtAmuPahWtaAz{EvxHdqBsSptQ~wJujArvD}rTq{N~pYwuDswXrnBdkm@}oXupIbyb@o|EiwHjnB}{BzoKqk[eeKtd`@yt^flC~d\\mqAk|B{c`@pvAanCenZ|aYv~[dcH_`g@sdPdhH~fQjzn@`mIeeNycIkxE_|Tl|D|`Ah~Bdr]gu\\sbDfnOd~CfcDu}El}Go{Gd`AbgJ`EaaGaaIev@ka@myMmc`@nuPhhq@cu^_{m@fi\\r|RhoNjtK_cNk~S|{EvbRrxAvnDs{c@mmh@puQhM{sB`it@rzP_cNi|DxlGteq@euFmpg@o~[ugFjkY}}GchYppGdfa@jmRuiUe|b@cwWh`Ebxk@~cPol`@quHlpd@~L_qi@enAnzBhnD~yS|vOwqRceSbeY_rG}rVjcGllTwo[}iV~{^f|o@psKylErzAmlMmjNhkAfnLp{C}oPfcLdsR{ur@exOz`@z_Adaa@oFcn_@~fCjzA}zEdwVa_TtuA~uVm~U|mC`|Tu{KomWefCyvQeYn_Oz|PcaJmyQp_l@rbHqej@af@rjo@|a\\cc@gwPivLaC``DunRsoPjyYgnWqpYpsj@piL_of@gyBbig@gw_@kgh@hx`@~tLrsHekCgeEdmXmaTqta@v}Gjdk@~wEejB|pCsa@l`By|J`gJ`|Arx@kh[qgSbf_@mq[~}Rnpl@cos@ccNpne@nqFkh@{aKkdBcuNqRtgVmn`@obBv`d@cpWyl\\fhZvjXchAj{Hfxr@{`Jwp{@fbCf{O{`]ok@bf^ku@r|@wna@{_Rhod@duPo`DuiRm`QwyJloH|eV_`\\nqNjff@~yHtsLcxo@quRdjk@~dJsmi@amPfd_@nvKqei@}gRnj_@fpQmvYu~LpzNfuJ_sL}mDbeN`gHxaMe|@oiAeqYz|BziOuc@p`PjbNwee@evFpl_@kjU}dK`cPxuJbAkca@laAhf`@ibOmuKqaXdfF~dAje@ry]iaSvlOn~y@y{h@oxb@|c@``DzkVvpFvdCqdGj{FhYssFs~Ei}Dp_RhmCigKppTdb]{bt@q}^fqEj}@nbYdqCey]afGzxa@qpAatVz{GbjHas[pjQpnUok`@q{Ac|@f~DqEs{Hd_a@jeV}yh@iuUnvHfxHp|XinG~{ClzIode@quBrfh@dxe@cdVaf|@iiVp{OtzAmeBf|e@pxCa_^obMuqAtqBjib@bnRcyd@apJvr[xtEj_DjdBeo]c_Jne[{nUum^`uUxhN_hKb`J}~OyyRf`a@|pYbx@xhEflFyxd@{wIbq]`h@{oFtzJmdPizJ|aRdaC}nGakOdeTlno@k}c@kuc@`fb@f`Fpo@cbDk|Faq`@bA|nd@}sd@qiWhrJj}WnuZavO_zZzzCnlJs}C~lCl~TqcOs}\\|oi@tg[_|NyyI{Dkp@d~PtiEyrp@ubGfq`@piEqrAlhCxlBetNqaBm~Nip@`b[ouL_lSo~Fd{Pje@vvF|ie@ddHe}H{}Nrd@~Jelk@igKpxIig@te@nmPrhE~lB~oTkeAnMkfKerGmjYbjH`im@ytf@yeQzqPycJg_C`qRbkG_sNgj@ulInyPx`Jw@fxH{t\\qkIryWmlTrkHrwa@cB~wEtbJ{mb@kiRbo\\ihKhc@|fNmhE~qJdm`@{`Hmd]_di@qoLtjp@|hSsuP~}BtgL}dJeua@wpBti@msOwiLnzFdqe@|sHkmUbbFajNydRz`ExpNxru@ddEayV{na@lIzcFdmAjg\\d`Hd__@_bVqed@xxQ|x@_g@zb@myVmbBozI_sHbpi@bxr@gah@aew@llOqdA_yRx|L~|EudFdLx{Gb~ItsHvtSyqKabl@ceKfbm@zpRgyEfeGso[gkWxvX~}Kkjd@crPjo^r|Oy~QcfFuxDtxCr}q@`}HssAuqj@i{h@pb^bq\\a{Fn{@twAc~Cw~MhlAzvVnbDoqCkxEukBagEngKzyEghHmlAdiHfcD}gHcnf@wy@wEcoFrdCd~Hj_GjHvt[waA~xFlgOq{i@iiXe{EtyGxnu@hjIsfr@suI|vIz_Enq_@~yAqs`@u}@e~GcjEz`GzGz~PdjNb}DscDe_Y}cGygCtIvba@e{DgxCnxGc~_@{qDhwIjiE}wHadGz}a@mqDscZrdCkg@je@t|m@rdHc|Pe|Yi_ZvuVxfIje@v_M{g[qaHpvf@vyT}lKwlClbFs}A{_Kve@~rJqrDwqc@bwGn~f@zPs{PorLatJf{F`gWotb@ggEq|ATkqCggKxvq@daOjxBohc@gsBt_oAgrFewx@vyKprZy`Nw~O{u_@e[rba@ie@scAnaBe_AwhUvgEp_Roc`@meGi~Bl{KdvIdHkeKqyEtnb@~jFe|f@kwPffGamDkUngKvf`@rrEn^reCs_@ccB`{PxmJk{Uwol@}fUbhd@dq\\dkG}mZesDzu^mXkp`@oeAi`JwdUdyd@hmIkwZ|xIdaZgg@crf@s{Fd{b@opNndEbnZb[q`E{_Cen_@|gOl`d@owp@wyDxlb@dxOtt@ctH_|HarDtrGpXcm[mmBseCrQxsTb`M|{Iu|HmeQgjRsiUdfFtlHhgNsWejErpRuoQunOl}Wtc@cnDnet@nrr@wv\\ixi@k|UapCssGn@t|^sf`@cg`@rmY``]||E_xZocJl|a@ej@wcAxrHso@|a@x`HjtBayGsxL}dLc_a@`kRvwt@wrCevIj]u_FlzM`{Komp@ktInk^gjCabi@_dDdxd@|rR}mOgi@";
    return EncodingUtils.decodePath(encodedData);
  }

  /**
   * Get (randomly_ weighted spatial data for use in tests
   * 
   * @return
   */
  @SuppressWarnings("unused")
  // here as an example of WeightedDataPoints
  private JsArray<WeightedLocation> getSampleWeightedData() {
    JsArray<LatLng> samplePoints = getSampleData();
    JsArray<WeightedLocation> sampleLocations = ArrayHelper.toJsArray(new WeightedLocation[] {});
    for (int n = 0, len = samplePoints.length(); n < len; n++) {
      sampleLocations.push(WeightedLocation.newInstance(samplePoints.get(n), 10 * Math.random()));
    }
    return sampleLocations;
  }

}