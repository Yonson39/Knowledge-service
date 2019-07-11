// Ext.BLANK_IMAGE_URL = 'resources/s.gif';

Docs = {};

ApiPanel = function() {
	ApiPanel.superclass.constructor.call(this, {
				id : 'api-tree',
				region : 'west',
				split : true,
				header : false,
				width : 220,
				minSize : 175,
				maxSize : 500,
				collapsible : true,
				margins : '0 0 5 5',
				cmargins : '0 0 0 0',
				rootVisible : true,
				lines : false,
				autoScroll : true,
				animCollapse : false,
				animate : false,
				collapseMode : 'header',
				loader : new Ext.tree.TreeLoader({
							preloadChildren : true,
							clearOnLoad : false
							,

						}),
				root : new Ext.tree.AsyncTreeNode({
							text : '工艺知识库',
							id : 'root',
							expanded : true,
							children : [Docs.classData1,Docs.classData2]
						}),						
				collapseFirst : false
			});
	// no longer needed!
	// new Ext.tree.TreeSorter(this, {folderSort:true,leafAttr:'isClass'});

	this.getSelectionModel().on('beforeselect', function(sm, node) {
				return node.isLeaf();
			});
};

Ext.extend(ApiPanel, Ext.tree.TreePanel, {
			initComponent : function() {
				this.hiddenPkgs = [];
				Ext.apply(this, {
							collapsible : true,
							
							tbar : [' ', new Ext.form.TextField({
										width : 120,
										emptyText : 'Find a Class',
										enableKeyEvents : true,
										listeners : {
											render : function(f) {
												this.filter = new Ext.tree.TreeFilter(
														this, {
															clearBlank : true,
															autoClear : true
														});
											},
											keydown : {
												fn : this.filterTree,
												buffer : 350,
												scope : this
											},
											scope : this
										}
									}), ' ', ' ', {
								iconCls : 'expand-allIcon',
								tooltip : 'Expand All',
								handler : function() {
									this.root.expand(true);
								},
								scope : this
							}, '-', {
								iconCls : 'collapse-allIcon',
								tooltip : 'Collapse All',
								handler : function() {
									this.root.collapse(true);
								},
								scope : this
							},'-', {
								iconCls : 'book_previousIcon',
								tooltip : 'Collapse All',
								handler : function() {
									this.collapse(true);
								},
								scope : this
							}]
						})
				ApiPanel.superclass.initComponent.call(this);
			},
			filterTree : function(t, e) {
				var text = t.getValue();
				Ext.each(this.hiddenPkgs, function(n) {
							n.ui.show();
						});
				if (!text) {
					this.filter.clear();
					return;
				}
				this.expandAll();

				var re = new RegExp(Ext.escapeRe(text), 'i');
				this.filter.filterBy(function(n) {
							return !n.attributes.isClass || re.test(n.text);
						});

				// hide empty packages that weren't filtered
				this.hiddenPkgs = [];
				var me = this;
				this.root.cascade(function(n) {
							if (!n.attributes.isClass
									&& n.ui.ctNode.offsetHeight < 3) {
								n.ui.hide();
								me.hiddenPkgs.push(n);
							}
						});
			},
			// 点击tab，与api树相连接
			selectClass : function(cls) {
				if (cls) {
					var parts = cls.split('.');
					var last = parts.length - 1;
					var res = [];
					var pkg = [];
					for (var i = 0; i < last; i++) { // things get nasty -
						// static classes can
						// have .
						var p = parts[i];
						var fc = p.charAt(0);
						var staticCls = fc.toUpperCase() == fc;
						if (p == 'Ext' || !staticCls) {
							pkg.push(p);
							res[i] = 'pkg-' + pkg.join('.');
						} else if (staticCls) {
							--last;
							res.splice(i, 1);
						}
					}
					res[last] = cls;

					this.selectPath('/root/apidocs/' + res.join('/'));
				}
			}
		});

DocPanel = Ext.extend(Ext.Panel, {
	closable : true,
	autoScroll : true,

	initComponent : function() {

		a = this.autoLoad.url.split('/');
		b = a[a.length - 1].split('.');
		this.title = b[0];

		Ext.apply(this, {});
		DocPanel.superclass.initComponent.call(this);
	},

	directLink : function() {
		var link = String.format("<a href=\"{0}\" target=\"_blank\">{0}</a>",
				document.location.href + '?class=' + this.cclass);
		Ext.Msg.alert('Direct Link to ' + this.cclass, link);
	},

	scrollToMember : function(member) {
		var el = Ext.fly(this.cclass + '-' + member);
		if (el) {
			var top = (el.getOffsetsTo(this.body)[1]) + this.body.dom.scrollTop;
			this.body.scrollTo('top', top - 25, {
						duration : 0.75,
						callback : this.hlMember.createDelegate(this, [member])
					});
		}
	},

	scrollToSection : function(id) {
		var el = Ext.getDom(id);
		if (el) {
			var top = (Ext.fly(el).getOffsetsTo(this.body)[1])
					+ this.body.dom.scrollTop;
			this.body.scrollTo('top', top - 25, {
						duration : 0.5,
						callback : function() {
							Ext.fly(el).next('h2').pause(0.2).highlight(
									'#8DB2E3', {
										attr : 'color'
									});
						}
					});
		}
	},

	hlMember : function(member) {
		var el = Ext.fly(this.cclass + '-' + member);
		if (el) {
			if (tr = el.up('tr')) {
				tr.highlight('#cadaf9');
			}
		}
	}
});

MainPanel = function() {

	MainPanel.superclass.constructor.call(this, {
				id : 'doc-body',
				region : 'center',
				margins : '0 5 5 0',
				resizeTabs : true,
				minTabWidth : 135,
				tabWidth : 135,
				plugins : new Ext.ux.TabCloseMenu(),
				enableTabScroll : true,
				activeTab : 0,

				items : {
					id : 'welcome-panel',
					title : '知识库主页面',
					autoLoad : {
						url : 'dec/knowledgebasesystem/welcome.html',
						callback : this.initSearch,
						scope : this
					},
					iconCls : 'icon-docs',
					autoScroll : true
				}
			});
};

Ext.extend(MainPanel, Ext.TabPanel, {

			initEvents : function() {
				MainPanel.superclass.initEvents.call(this);
				this.body.on('click', this.onClick, this);
			},

			onClick : function(e, target) {
				if (target = e.getTarget('a:not(.exi)', 3)) {
					var cls = Ext.fly(target).getAttributeNS('ext', 'cls');
					e.stopEvent();
					if (cls) {
						var member = Ext.fly(target).getAttributeNS('ext',
								'member');
						this.loadClass(target.href, cls, member);
					} else if (target.className == 'inner-link') {
						this.getActiveTab().scrollToSection(target.href
								.split('#')[1]);
					} else {
						window.open(target.href);
					}
				} else if (target = e.getTarget('.micon', 2)) {
					e.stopEvent();
					var tr = Ext.fly(target.parentNode);
					if (tr.hasClass('expandable')) {
						tr.toggleClass('expanded');
					}
				}
			},

			loadClass : function(href, cls, member) {
				var id = 'docs-' + cls;
				var tab = this.getComponent(id);
				if (tab) {
					this.setActiveTab(tab);
					if (member) {
						tab.scrollToMember(member);
					}
				} else {
					var autoLoad = {
						url : href
					};
					if (member) {
						autoLoad.callback = function() {
							Ext.getCmp(id).scrollToMember(member);
						}
					}
					var p = this.add(new DocPanel({
								id : id,
								cclass : cls,
								autoLoad : autoLoad,
								iconCls : Docs.icons[cls]
							}));
					this.setActiveTab(p);
				}
			}
		});

Ext.onReady(function() {

	Ext.QuickTips.init();

	var api = new ApiPanel();
	var mainPanel = new MainPanel();

	api.on('click', function(node, e) {

				e.stopEvent();
				mainPanel.loadClass(node.attributes.href, node.id);

			});

	mainPanel.on('tabchange', function(tp, tab) {
				api.selectClass(tab.cclass);
			});



	viewport = new Ext.Viewport({
				layout : 'border',
				items : [api,mainPanel]
			});
	viewport.doLayout();
	/*
	 * var viewport = new Ext.Viewport({ layout:'border', items:[ { cls:
	 * 'docs-header', height: 44, region:'north', xtype:'box', el:'header',
	 * border:false, margins: '0 0 5 0' }, api, mainPanel ] });
	 */

	api.expandPath('/root/apidocs');

		// allow for link in
		/*
		 * var page = window.location.href.split('?')[1]; if (page) { var ps =
		 * Ext.urlDecode(page); var cls = ps['class'];
		 * mainPanel.loadClass('output/' + cls + '.html', cls, ps.member); }
		 */

		/*
		 * viewport.doLayout();
		 * 
		 * setTimeout(function(){ Ext.get('loading').remove();
		 * Ext.get('loading-mask').fadeOut({remove:true}); }, 250);
		 */

	});

/**
 * Makes a ComboBox more closely mimic an HTML SELECT. Supports clicking and
 * dragging through the list, with item selection occurring when the mouse
 * button is released. When used will automatically set {@link #editable} to
 * false and call {@link Ext.Element#unselectable} on inner elements.
 * Re-enabling editable after calling this will NOT work.
 * 
 * @author Corey Gilmore http://extjs.com/forum/showthread.php?t=6392
 * 
 * @history 2007-07-08 jvs Slight mods for Ext 2.0
 */
Ext.ux.SelectBox = function(config) {
	this.searchResetDelay = 1000;
	config = config || {};
	config = Ext.apply(config || {}, {
				editable : false,
				forceSelection : true,
				rowHeight : false,
				lastSearchTerm : false,
				triggerAction : 'all',
				mode : 'local'
			});

	Ext.ux.SelectBox.superclass.constructor.apply(this, arguments);

	this.lastSelectedIndex = this.selectedIndex || 0;
};

Ext.extend(Ext.ux.SelectBox, Ext.form.ComboBox, {
			lazyInit : false,
			initEvents : function() {
				Ext.ux.SelectBox.superclass.initEvents.apply(this, arguments);
				// you need to use keypress to capture upper/lower case and
				// shift+key, but it doesn't work in IE
				this.el.on('keydown', this.keySearch, this, true);
				this.cshTask = new Ext.util.DelayedTask(
						this.clearSearchHistory, this);
			},

			keySearch : function(e, target, options) {
				var raw = e.getKey();
				var key = String.fromCharCode(raw);
				var startIndex = 0;

				if (!this.store.getCount()) {
					return;
				}

				switch (raw) {
					case Ext.EventObject.HOME :
						e.stopEvent();
						this.selectFirst();
						return;

					case Ext.EventObject.END :
						e.stopEvent();
						this.selectLast();
						return;

					case Ext.EventObject.PAGEDOWN :
						this.selectNextPage();
						e.stopEvent();
						return;

					case Ext.EventObject.PAGEUP :
						this.selectPrevPage();
						e.stopEvent();
						return;
				}

				// skip special keys other than the shift key
				if ((e.hasModifier() && !e.shiftKey) || e.isNavKeyPress()
						|| e.isSpecialKey()) {
					return;
				}
				if (this.lastSearchTerm == key) {
					startIndex = this.lastSelectedIndex;
				}

				this.cshTask.delay(this.searchResetDelay);
			},

			onRender : function(ct, position) {
				this.store.on('load', this.calcRowsPerPage, this);
				Ext.ux.SelectBox.superclass.onRender.apply(this, arguments);
				if (this.mode == 'local') {
					this.calcRowsPerPage();
				}
			},

			onSelect : function(record, index, skipCollapse) {
				if (this.fireEvent('beforeselect', this, record, index) !== false) {
					this.setValue(record.data[this.valueField
							|| this.displayField]);
					if (!skipCollapse) {
						this.collapse();
					}
					this.lastSelectedIndex = index + 1;
					this.fireEvent('select', this, record, index);
				}
			},

			render : function(ct) {
				Ext.ux.SelectBox.superclass.render.apply(this, arguments);
				if (Ext.isSafari) {
					this.el.swallowEvent('mousedown', true);
				}
				this.el.unselectable();
				this.innerList.unselectable();
				this.trigger.unselectable();
				this.innerList.on('mouseup', function(e, target, options) {
							if (target.id && target.id == this.innerList.id) {
								return;
							}
							this.onViewClick();
						}, this);

				this.innerList.on('mouseover', function(e, target, options) {
							if (target.id && target.id == this.innerList.id) {
								return;
							}
							this.lastSelectedIndex = this.view
									.getSelectedIndexes()[0]
									+ 1;
							this.cshTask.delay(this.searchResetDelay);
						}, this);

				this.trigger.un('click', this.onTriggerClick, this);
				this.trigger.on('mousedown', function(e, target, options) {
							e.preventDefault();
							this.onTriggerClick();
						}, this);

				this.on('collapse', function(e, target, options) {
							Ext.getDoc().un('mouseup', this.collapseIf, this);
						}, this, true);

				this.on('expand', function(e, target, options) {
							Ext.getDoc().on('mouseup', this.collapseIf, this);
						}, this, true);
			},

			clearSearchHistory : function() {
				this.lastSelectedIndex = 0;
				this.lastSearchTerm = false;
			},

			selectFirst : function() {
				this.focusAndSelect(this.store.data.first());
			},

			selectLast : function() {
				this.focusAndSelect(this.store.data.last());
			},

			selectPrevPage : function() {
				if (!this.rowHeight) {
					return;
				}
				var index = Math.max(this.selectedIndex - this.rowsPerPage, 0);
				this.focusAndSelect(this.store.getAt(index));
			},

			selectNextPage : function() {
				if (!this.rowHeight) {
					return;
				}
				var index = Math.min(this.selectedIndex + this.rowsPerPage,
						this.store.getCount() - 1);
				this.focusAndSelect(this.store.getAt(index));
			},

			focusAndSelect : function(record) {
				var index = typeof record === 'number' ? record : this.store
						.indexOf(record);
				this.select(index, this.isExpanded());
				this.onSelect(this.store.getAt(record), index, this
								.isExpanded());
			},

			calcRowsPerPage : function() {
				if (this.store.getCount()) {
					this.rowHeight = Ext.fly(this.view.getNode(0)).getHeight();
					this.rowsPerPage = this.maxHeight / this.rowHeight;
				} else {
					this.rowHeight = false;
				}
			}

		});

Ext.Ajax.on('requestcomplete', function(ajax, xhr, o) {
			if (typeof urchinTracker == 'function' && o && o.url) {
				urchinTracker(o.url);
			}
		});