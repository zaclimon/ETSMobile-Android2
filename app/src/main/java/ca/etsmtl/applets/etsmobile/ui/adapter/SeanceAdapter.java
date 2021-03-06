package ca.etsmtl.applets.etsmobile.ui.adapter;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import ca.etsmtl.applets.etsmobile.model.Event;
import ca.etsmtl.applets.etsmobile.model.IHoraireRows;
import ca.etsmtl.applets.etsmobile.model.Seances;
import ca.etsmtl.applets.etsmobile.util.HoraireComparator;
import ca.etsmtl.applets.etsmobile2.R;

public class SeanceAdapter extends BaseAdapter {

    private List<TodayDataRowItem> listSeances;
    private HashMap<String,Integer> colors;
    private int indexColor = 0;
    private int[] rainbow;

    private Context context;

    public SeanceAdapter(Context context) {
        this.context = context;
        listSeances = new ArrayList<>();
        colors = new HashMap<>();
        rainbow = context.getResources().getIntArray(R.array.rainbow);
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }
    @Override
    public int getCount() {
        return listSeances.size();
    }

    @Override
    public Object getItem(int position) {
        return listSeances.get(position).data;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return listSeances.get(position).type;
    }

    @Override
    public int getViewTypeCount() {
        return TodayDataRowItem.viewType.values().length;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int viewType = getItemViewType(position);

        if (convertView == null) {

            if (viewType == TodayDataRowItem.viewType.VIEW_TYPE_TITLE_SEANCE.getValue()) {
                convertView = LayoutInflater.from(context).inflate(R.layout.row_today_title, parent,false);
                ViewSeancesTitleHolder titleHolder = new ViewSeancesTitleHolder();
                titleHolder.tvTitle = (TextView) convertView.findViewById(R.id.todays_title);
                convertView.setTag(titleHolder);

            } else if (viewType == TodayDataRowItem.viewType.VIEW_TYPE_SEANCE.getValue()) {
                convertView = LayoutInflater.from(context).inflate(R.layout.row_today_courses, parent, false );
                ViewSeancesHolder seancesHolder = new ViewSeancesHolder();
                seancesHolder.tvHeureDebut = (TextView) convertView.findViewById(R.id.tv_today_heure_debut);
                seancesHolder.tvHeureFin = (TextView) convertView.findViewById(R.id.tv_today_heure_fin);
                seancesHolder.tvCoursGroupe = (TextView) convertView.findViewById(R.id.tv_today_cours_groupe);
                seancesHolder.tvNomActivite = (TextView) convertView.findViewById(R.id.tv_today_nom_activite);
                seancesHolder.tvLibelleCours = (TextView) convertView.findViewById(R.id.tv_today_libelle_cours);
                seancesHolder.tvLocal = (TextView) convertView.findViewById(R.id.tv_today_local);
                seancesHolder.tvSeparator = (TextView) convertView.findViewById(R.id.tv_vertical_separator);
                convertView.setTag(seancesHolder);
            } else if (viewType == TodayDataRowItem.viewType.VIEW_TYPE_ETS_EVENT.getValue()){
                convertView = LayoutInflater.from(context).inflate(R.layout.row_today_ets_event, parent, false );
                ViewEvenementETSHolder etsEventHolder = new ViewEvenementETSHolder();
                etsEventHolder.tvLibelleEvenementETS = (TextView) convertView.findViewById(R.id.tv_today_libelle_ets_evenement);
                convertView.setTag(etsEventHolder);
            }
        }

        if (viewType == TodayDataRowItem.viewType.VIEW_TYPE_TITLE_SEANCE.getValue()) {
            ViewSeancesTitleHolder titleHolder = (ViewSeancesTitleHolder) convertView.getTag();
            titleHolder.tvTitle.setText((String) getItem(position));
        } else if (viewType == TodayDataRowItem.viewType.VIEW_TYPE_SEANCE.getValue()) {
            Seances seance = (Seances) getItem(position);
            ViewSeancesHolder viewSeancesHolder = (ViewSeancesHolder) convertView.getTag();
            viewSeancesHolder.tvNomActivite.setText(seance.nomActivite);
            viewSeancesHolder.tvLibelleCours.setText(seance.libelleCours);
            viewSeancesHolder.tvCoursGroupe.setText(seance.coursGroupe);
            viewSeancesHolder.tvLocal.setText(seance.local);

            if(colors.containsKey(seance.coursGroupe)) {
                viewSeancesHolder.tvSeparator.setBackgroundColor(colors.get(seance.coursGroupe));
            } else {

                colors.put(seance.coursGroupe,rainbow[indexColor%rainbow.length]);
                viewSeancesHolder.tvSeparator.setBackgroundColor(rainbow[indexColor%rainbow.length]);
                indexColor++;

            }

            DateTime mDateDebut = DateTime.parse(seance.dateDebut);
            DateTime mDateFin = DateTime.parse(seance.dateFin);

            String dateDebut = String.format("%dh%02d", mDateDebut.getHourOfDay(), mDateDebut.getMinuteOfHour());
            String dateFin = String.format("%dh%02d", mDateFin.getHourOfDay(), mDateFin.getMinuteOfHour());

            viewSeancesHolder.tvHeureDebut.setText(dateDebut);
            viewSeancesHolder.tvHeureFin.setText(dateFin);
        } else if (viewType == TodayDataRowItem.viewType.VIEW_TYPE_ETS_EVENT.getValue()) {
            Event event = (Event) getItem(position);
            ViewEvenementETSHolder viewSeancesHolder = (ViewEvenementETSHolder) convertView.getTag();
            viewSeancesHolder.tvLibelleEvenementETS.setText(event.getTitle());
        }



        return convertView;
    }

    public List<TodayDataRowItem> getItemList() {
        return listSeances;
    }

    public void setItemList(List<Seances> itemListSeance, List<Event> itemListEvent) {

        listSeances = new ArrayList<>();
        ArrayList<IHoraireRows> listRowItems = new ArrayList<IHoraireRows>();
        String tempDate = "";
        DateTime today = new DateTime();

        listRowItems.addAll(itemListEvent);
        listRowItems.addAll(itemListSeance);

        Collections.sort(listRowItems, new HoraireComparator());

        for(IHoraireRows rows : listRowItems) {

            DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
            DateTime seanceDay = formatter.parseDateTime(rows.getDateDebut().substring(0, 10));

//            if(today.isAfter(seanceDay) && !DateUtils.isToday(seanceDay.getMillis()) ) {
//                continue;
//            }

            if(!rows.getDateDebut().substring(0,10).equals(tempDate)) {

                tempDate = rows.getDateDebut().substring(0,10);

                DateTime.Property pDoW = seanceDay.dayOfWeek();
                DateTime.Property pDoM = seanceDay.dayOfMonth();
                DateTime.Property pMoY = seanceDay.monthOfYear();

                this.listSeances.add(new TodayDataRowItem(TodayDataRowItem.viewType.VIEW_TYPE_TITLE_SEANCE, context.getString(R.string.date_text, pDoW.getAsText(Locale.getDefault()), pDoM.get(), pMoY.getAsText(Locale.getDefault()))));
            }

            if(rows.getClass().equals(Event.class)){
                this.listSeances.add(new TodayDataRowItem(TodayDataRowItem.viewType.VIEW_TYPE_ETS_EVENT, rows));
            } else if(rows.getClass().equals(Seances.class)){
                this.listSeances.add(new TodayDataRowItem(TodayDataRowItem.viewType.VIEW_TYPE_SEANCE, rows));
            }
        }

    }

    static class ViewSeancesHolder {
        TextView tvHeureDebut;
        TextView tvHeureFin;
        TextView tvNomActivite;
        TextView tvCoursGroupe;
        TextView tvLibelleCours;
        TextView tvLocal;
        TextView tvSeparator;
    }

    static class ViewSeancesTitleHolder {
        TextView tvTitle;
    }

    static class ViewEvenementETSHolder {
        TextView tvLibelleEvenementETS;
    }
}
